package hw1.structure;

import java.io.Serializable;
import java.util.UUID;

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

    public Commit(String message, String branchName) {
        this.message = message;
        this.parentId = id;
        this.branchName = branchName;
    }

    public Commit(String message, String parentId, Branch branchName) {
        this.message = message;
        this.parentId = parentId;
        this.branchName = branchName.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Commit)) return false;

        Commit commit = (Commit) o;

        return id.equals(commit.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
