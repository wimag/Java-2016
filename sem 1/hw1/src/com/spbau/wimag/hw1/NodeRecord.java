package com.spbau.wimag.hw1;

/**
 * Created by Mark on 18.02.2016.
 * Simple key-value pair storage
 */
public class NodeRecord {
    private final char key;
    private final Node value;

    public NodeRecord(char key, Node value) {
        this.key = key;
        this.value = value;
    }

    public Node getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeRecord that = (NodeRecord) o;

        return key == that.key;

    }

    @Override
    public int hashCode() {
        return (int) key;
    }

}
