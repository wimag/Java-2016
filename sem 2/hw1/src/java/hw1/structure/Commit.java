package hw1.structure;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Mark on 22.09.2016.
 * <p>
 * Class represents a sing commit. It holds
 * information about commit message, it's ID,
 * parent commit, branch name etc.
 */

public class Commit implements Serializable {
    public final String message;
    public final String id = UUID.randomUUID().toString();
    public final String parentId;
    public final String branchName;
    private Map<String, String> files = new HashMap<>();

    Commit(String message, String branchName) {
        this.message = message;
        this.parentId = id;
        this.branchName = branchName;
    }

    Commit(String message, String parentId, Branch branch) {
        this.message = message;
        this.parentId = parentId;
        this.branchName = branch.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Commit)) return false;

        Commit commit = (Commit) o;

        return id.equals(commit.id);
    }

    /**
     * check if file was tracked in this commit
     * @param file - to check
     * @return true if file already existed,
     * false otherwise
     */
    public boolean containsFile(String file){
        return files.containsKey(file);
    }

    /**
     * get revision of specified file
     * @param file - whose revision to check
     * @return - String representation of revision
     * or null if no such file was tracked
     */
    public String getFileRevision(String file){
        return files.get(file);
    }

    /**
     * register file to this commit
     * @param file - new file
     * @param revision - revision to be stored
     */
    public void addFile(String file, String revision){
        files.put(file, revision);
    }

    /**
     * get all files tracked in this commit
     * @return - List containing paths to all
     * files that were tracked in this commit
     */
    public List<String> getFiles(){
        return new ArrayList<>(files.keySet());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
