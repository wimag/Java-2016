package hw1.structure;

import hw1.GutUtils;
import hw1.exceptions.DuplicateBranchException;
import hw1.exceptions.MalformedCommandException;
import hw1.exceptions.NoSuchBranchException;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Mark on 22.09.2016.
 * <p>
 * Class for operation and serializing commit DAG strucure.
 */
public class StateManager implements Serializable {
    private final transient static String filename = "tree.dump";
    private Map<String, Commit> idToCommit = new HashMap<>();
    private Set<Branch> branches = new HashSet<>();
    private String headId = null;
    private Branch currentBranch = null;

    /**
     * Load data from dump file
     *
     * @throws IOException            - if dump file is inaccessible
     * @throws ClassNotFoundException - if dump file is corrupted
     */
    public static StateManager load() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getDumpFilePath()));
        StateManager sm = (StateManager) ois.readObject();
        ois.close();
        return sm;
    }

    /**
     * @return path to dump file
     */
    private static String getDumpFilePath() {
        return Paths.get(GutUtils.getRepoPath(), filename).toString();
    }

    /**
     * create a new branch with given name
     *
     * @param branchName - name of branchName to create
     */
    public void createBranch(String branchName) throws DuplicateBranchException {
        Branch branch = new Branch(branchName, headId);
        if (!branches.add(branch)) {
            throw new DuplicateBranchException();
        }
        currentBranch = branch;
        System.out.println("Switched to branch " + branchName + "\n");
    }

    /**
     * delete a branch with given name
     *
     * @param branchName - name of branchName to delete
     */
    public void deleteBranch(String branchName) throws IOException {
        Iterator it = idToCommit.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry current = (Map.Entry) it.next();
            Commit commit = (Commit) current.getValue();
            if (commit.branchName.equals(branchName)) {
                deleteBackup(commit);
                it.remove();
            }
        }
        branches.removeIf(x -> x.name.equals(branchName));
    }

    /**
     * add Given commit to our structure
     *
     * @param commit - freshly created commit
     */
    public void commit(Commit commit) throws IOException {
        idToCommit.put(commit.id, commit);
        createBackup(commit);
        headId = commit.id;
        currentBranch.setHead(commit.id);
        System.out.println("Added new commit. Current head is " + commit.id + "\n");
    }

    /**
     * Merge other branch into current one.
     *
     * @param branchName - name of branch to merge,
     *                   changes in this branch may overrite current files
     * @throws MalformedCommandException
     * @throws IOException
     */
    public void mergeBranch(String branchName) throws MalformedCommandException, IOException {
        Branch toMerge = getBranchByName(branchName);
        if (toMerge.equals(currentBranch)) {
            throw new MalformedCommandException("Can not merge branch into itself");
        }
        merge(idToCommit.get(currentBranch.getHead()), idToCommit.get(toMerge.getHead()));
    }

    /**
     * Close this StateManager and dump data to file
     *
     * @throws IOException - if dump file is inaccessible
     */
    public void close() throws IOException {
        Path path = Paths.get(GutUtils.getRepoPath());
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getDumpFilePath()));
        oos.writeObject(this);
        oos.flush();
        oos.close();
    }

    /**
     * @return current head ID
     */
    public String getHeadId() {
        return headId;
    }

    /**
     * @return current branchName
     */
    public Branch getCurrentBranch() {
        return currentBranch;
    }

    /**
     * Get Commit with given ID
     *
     * @param commitID - id of commit to find
     * @return = actual commit
     */
    public Commit getCommitByID(String commitID) {
        return idToCommit.get(commitID);
    }

    /**
     * Checkout given commit
     *
     * @param commitID - commit to checkout
     * @throws IOException           - if IO failes
     * @throws NoSuchBranchException
     */
    public void checkoutCommit(String commitID) throws IOException, NoSuchBranchException {
        Commit commit = idToCommit.get(commitID);
        restoreBackup(commit);
        headId = commitID;
        currentBranch = getBranchByName(commit.branchName);
    }

    /**
     * checkout given branch
     *
     * @param branchName - name of branch to checkout
     * @throws NoSuchBranchException
     * @throws IOException
     */
    public void checkoutBranch(String branchName) throws NoSuchBranchException, IOException {
        Branch branch = getBranchByName(branchName);
        checkoutCommit(branch.getHead());
        currentBranch = branch;
    }

    /**
     * Get branch with given name
     *
     * @param name - name of branch to find
     * @return Found branch
     * @throws NoSuchBranchException - if no branch found
     */
    private Branch getBranchByName(String name) throws NoSuchBranchException {
        for (Branch branch : branches) {
            if (Objects.equals(branch.name, name)) {
                return branch;
            }
        }
        throw new NoSuchBranchException();
    }

    /**
     * Restore a single commit files
     *
     * @param commit - commit ot restore
     * @throws IOException
     */
    private void restoreBackup(Commit commit) throws IOException {
        clearProjectFiles();
        Path path = Paths.get(GutUtils.getRepoPath(), commit.id);
        File dst = new File(GutUtils.getCurrentPath());
        File src = new File(path.toString());
        FileUtils.copyDirectory(src, dst, pathname -> path.startsWith(GutUtils.getRepoPath()));
    }

    /**
     * remove all files from current state
     */
    private void clearProjectFiles() {
        File folder = new File(GutUtils.getCurrentPath());
        FileUtils.iterateFiles(folder, null, true).forEachRemaining((File x) -> {
            if (!x.getAbsolutePath().startsWith(GutUtils.getRepoPath())) {
                try {
                    FileUtils.forceDelete(x);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create backup of files at current state
     *
     * @param commit - current commit
     * @throws IOException
     */
    private void createBackup(Commit commit) throws IOException {
        Path path = Paths.get(GutUtils.getRepoPath(), commit.id);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        File src = new File(GutUtils.getCurrentPath());
        File dst = new File(path.toString());
        FileUtils.copyDirectory(src, dst, pathname -> !pathname.getAbsolutePath().startsWith(GutUtils.getRepoPath()));
    }

    /**
     * Delete files of given commit
     *
     * @param commit - commit to clear
     * @throws IOException - if IO operations were
     *                     not successful
     */
    private void deleteBackup(Commit commit) throws IOException {
        Path path = Paths.get(GutUtils.getRepoPath(), commit.id);
        if (Files.exists(path)) {
            FileUtils.deleteDirectory(new File(path.toString()));
        }
    }

    /**
     * Merge two commits. For now - simpliest policy ever:
     * checkout first commit. then copy all files of second commit.
     * override conflicts
     *
     * @param commit1
     * @param commit2
     * @throws IOException
     */
    private void merge(Commit commit1, Commit commit2) throws IOException {
        restoreBackup(commit1);
        Path path = Paths.get(GutUtils.getRepoPath(), commit2.id);
        File dst = new File(GutUtils.getCurrentPath());
        File src = new File(path.toString());
        FileUtils.copyDirectory(src, dst, pathname -> path.startsWith(GutUtils.getRepoPath()));
    }


}
