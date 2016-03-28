package com.spbau.wimag.hw1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 18.02.2016.
 * Node of Trie.
 */
public class Node {


    private boolean isWordEnd;
    private int entries = 1;
    private final List<NodeRecord> children = new ArrayList<>();

    /**
     * get child Node for provided character
     *
     * @param c - key for child Node
     * @return {@code Node} object, corresponding Trie vertex
     */
    public Node get(char c) {
        int idx = getIndex(c);
        if (idx != -1) {
            return children.get(idx).getValue();
        }
        return null;
    }


    /**
     * Inserts character as child of this Node.
     * If a child node with given character key
     * already exists - increases it's internal
     * entry counter by one
     *
     * @param c - character key for new child
     * @return - child with corresponding key(
     * either existing or newly created)
     * @see Node#getEntryCount()
     */
    public Node insert(char c) {
        int idx = getIndex(c);
        if (idx == -1) {
            NodeRecord record = new NodeRecord(c, new Node());
            children.add(record);
            return record.getValue();
        }
        children.get(idx).getValue().increaseEntryCount();
        return children.get(idx).getValue();
    }

    /**
     * remove a child node for given character key:
     * if there is a child with given key - reduce it's
     * entryCount by one. if it has no more entries - remove
     * corresponding {@code NodeRecord} from children list
     *
     * @param c
     * @return if Node has no children with given key -
     * return null, else return removed child
     * @see Node#getEntryCount()
     */
    public Node remove(char c) {
        int idx = getIndex(c);
        if (idx == -1) {
            return null;
        }
        Node res = children.get(idx).getValue();
        res.decreaseEntryCount();
        if (res.getEntryCount() == 0) {
            children.remove(idx);
        }
        return res;
    }

    /**
     * set/unset this Node as word and
     *
     * @param wordEndStatus - value to be set
     */
    public void setWordEndStatus(boolean wordEndStatus) {
        isWordEnd = wordEndStatus;
    }

    public boolean isWordEnd() {
        return isWordEnd;
    }

    /**
     * Describes entryCount of given node - number
     * of times, this Node was added
     * basically, number of words, that start with
     * this prefix
     *
     * @return entryCount
     */
    public int getEntryCount() {
        return entries;
    }

    private void increaseEntryCount() {
        entries++;
    }

    private void decreaseEntryCount() {
        entries--;
    }

    /**
     * Finds position of record in the inner array-like storage
     *
     * @param c - char to find
     * @return - index of corresponding NodeRecord in {@code children}
     */
    private int getIndex(char c) {
        return children.indexOf(new NodeRecord(c, new Node()));
    }
}
