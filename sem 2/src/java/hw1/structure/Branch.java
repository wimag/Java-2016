package hw1.structure;

import java.io.Serializable;

/**
 * Created by Mark on 22.09.2016.
 * <p>
 * class represents a branch in our vcs system.
 * Only necessary information is branch name and
 * id of head commit
 */
public class Branch implements Serializable {
    public final String name;
    private String head;

    public Branch(String name) {
        this(name, null);
    }

    Branch(String name, String headId) {
        this.name = name;
        this.head = headId;
    }

    String getHead() {
        return head;
    }

    void setHead(String head) {
        this.head = head;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Branch)) return false;

        Branch branch = (Branch) o;

        return name != null ? name.equals(branch.name) : branch.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
