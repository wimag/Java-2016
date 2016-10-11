package hw1.structure;

import hw1.GutUtils;
import hw1.command.Command;
import hw1.exceptions.DuplicateBranchException;
import hw1.exceptions.MalformedCommandException;
import hw1.exceptions.NoSuchBranchException;
import hw1.exceptions.UntrackedFileException;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mark on 22.09.2016.
 * <p>
 * Class for operation and serializing commit DAG strucure.
 */
public class StateManager implements Serializable {
    private final transient static String filename = "tree.dump";
    private Map<String, Commit> idToCommit = new HashMap<>();
    private Set<Branch> branches = new HashSet<>();
    private Set<String> trackedFiles = new HashSet<>();
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
     * @param branchName - name of branch to delete
     */
    public void deleteBranch(String branchName) throws IOException {
        Iterator it = idToCommit.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry current = (Map.Entry) it.next();
            Commit commit = (Commit) current.getValue();
            if (commit.branchName.equals(branchName)) {
                it.remove();
            }
        }
        branches.removeIf(x -> x.name.equals(branchName));
    }

    /**
     * create a commit with given message
     * @param message - message appended to new commit
     * @return id of new commit
     * @throws IOException
     */
    public String commit(String message) throws IOException {
        Commit newCommit = new Commit(message, headId, currentBranch);
        commit(newCommit);
        return newCommit.id;
    }

    /**
     * init this repository
     * @param masterBranchName - name of master branch
     * @throws DuplicateBranchException
     * @throws IOException
     */
    public void init(String masterBranchName) throws DuplicateBranchException, IOException {
        createBranch(masterBranchName);
        commit(new Commit("Initial commit", masterBranchName));
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
     * mark file as tracked
     * @param path to file
     */
    public void add(String path) {
        if(Files.exists(Paths.get(path))){
            trackedFiles.add(new File(path).getAbsolutePath());
        }
    }

    /**
     * force remove file from repository
     * and file system
     * @param path to file
     * @throws IOException
     */
    public void remove(String path) throws IOException {
        File file = new File(path);
        trackedFiles.remove(file.getAbsolutePath());
        if(Files.exists(Paths.get(path))){
            FileUtils.forceDelete(file);
        }
    }

    /**
     * reset file to it latest repository version
     * @param path to file to reset
     */
    public void reset(String path) throws UntrackedFileException, IOException {
        String absolutePath = new File(path).getAbsolutePath();
        Commit head = idToCommit.get(headId);
        if(!head.containsFile(absolutePath)){
            throw new UntrackedFileException();
        }
        FileUtils.copyFile(new File(getFileRevisoinPath(head.getFileRevision(absolutePath))), new File(path));
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
     * get list of tracked files that changes
     * from last commit
     * @return list of changed files
     * @throws IOException
     */
    public List<String> getChangedFiles() throws IOException {
        List<String> result = new ArrayList<>();
        Commit head = idToCommit.get(headId);
        for(String file: trackedFiles){
            String revision = head.getFileRevision(file);
            if(head.containsFile(file) && !GutUtils.equalFiles(file, getFileRevisoinPath(revision))){
                result.add(file);
            }else if(!head.containsFile(file) && Files.exists(Paths.get(file))){
                result.add(file);
            }
        }
        return result;
    }

    /**
     * get List of files that were deleted from last commit
     * @return list of file paths
     */
    public List<String> getDeletedFiles(){
        return trackedFiles.stream().filter(x -> !Files.exists(Paths.get(x))).collect(Collectors.toList());
    }

    /**
     * get List of files that are not in the repository
     * @return list of file paths
     */
    public List<String> getUntrackedFiles(){
        return FileUtils.listFiles(new File(GutUtils.getCurrentPath()), null, true).stream()
                .map(File::getAbsolutePath)
                .filter(x -> !trackedFiles.contains(x) && !x.startsWith(GutUtils.getRepoPath()))
                .collect(Collectors.toList());
    }

    /**
     * remove all untracked files from project
     */
    public void cleanProjectFiles() {
        File folder = new File(GutUtils.getCurrentPath());
        FileUtils.iterateFiles(folder, null, true).forEachRemaining((File x) -> {
            if (!x.getAbsolutePath().startsWith(GutUtils.getRepoPath()) && !trackedFiles.contains(x)) {
                try {
                    FileUtils.forceDelete(x);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * add Given commit to our structure
     *
     * @param commit - commit to be added to vcs
     */
    private void commit(Commit commit) throws IOException {
        idToCommit.put(commit.id, commit);
        createBackup(commit);
        headId = commit.id;
        currentBranch.setHead(commit.id);
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
     * Create backup of files at current state
     *
     * @param commit - current commit
     * @throws IOException
     */
    private void createBackup(Commit commit) throws IOException {
        Commit parent = idToCommit.get(commit.parentId);
        for(String file: trackedFiles){
            if (!Files.exists(Paths.get(file))){
                continue;
            }
            String revision;
            String parentRevision = parent.getFileRevision(file);
            if(parent.containsFile(file) && GutUtils.equalFiles(getFileRevisoinPath(parentRevision), file)){
                revision = parent.getFileRevision(file);
            }else{
                revision = UUID.randomUUID().toString();
                FileUtils.copyFile(new File(file), new File(getFileRevisoinPath(revision)));
            }
            commit.addFile(file, revision);
        }
    }

    /**
     * Restore a single commit files
     *
     * @param commit - commit ot restore
     * @throws IOException
     */
    private void restoreBackup(Commit commit) throws IOException {
        for(String file: commit.getFiles()){
            String backupPath = getFileRevisoinPath(commit.getFileRevision(file));
            FileUtils.copyFile(new File(backupPath), new File(file));
        }
    }


    /**
     * Merge two commits. For now - simplest policy ever:
     * checkout first commit. then copy all absent files of second commit.
     * override conflicts
     *
     * @param commit1
     * @param commit2
     * @throws IOException
     */
    private void merge(Commit commit1, Commit commit2) throws IOException {
        restoreBackup(commit1);
        for (String file: commit2.getFiles()){
            if(!commit1.containsFile(file)){
                String backupPath = getFileRevisoinPath(commit2.getFileRevision(file));
                FileUtils.copyFile(new File(backupPath), new File(file));
            }
        }
    }

    /**
     * get path to file backup by his revision
     * @param revision - id of revision
     * @return path to backup
     */
    private String getFileRevisoinPath(String revision){
        return Paths.get(GutUtils.getRepoPath(), revision).toString();
    }
}
