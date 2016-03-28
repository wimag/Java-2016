package com.spbau.wimag.hw1;

/**
 * Created by Mark on 18.02.2016.
 */
public class TrieImpl implements Trie{
    private final Node root = new Node();

    private int size;

    /**
     * {@inheritDoc}
     * @param element
     * @return
     */
    @Override
    public boolean add(String element) {
        if(contains(element)){
            return false;
        }
        Node currentNode = root;
        for(char c: element.toCharArray()){
            currentNode = currentNode.insert(c);
        }
        currentNode.setWordEnd(true);
        size++;
        return true;
    }

    /**
     * {@inheritDoc}
     * @param element
     * @return
     */
    @Override
    public boolean contains(String element) {
        Node currentNode = root;
        for(char c: element.toCharArray()){
            currentNode = currentNode.get(c);
            if (currentNode == null) {
                return false;
            }
        }
        return currentNode.isWordEnd();
    }

    /**
     * {@inheritDoc}
     * @param element
     * @return
     */
    @Override
    public boolean remove(String element) {
        if(!contains(element)){
            return false;
        }
        Node currentNode = root;
        for(char c: element.toCharArray()){
            currentNode = currentNode.remove(c);
            if(currentNode == null){
                return false;
            }
        }
        size--;
        currentNode.setWordEnd(false);

        return true;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * {@inheritDoc}
     * @param prefix
     * @return
     */
    @Override
    public int howManyStartsWithPrefix(String prefix) {
        if(prefix.isEmpty()){
            return size();
        }
        Node currentNode = root;
        for (char c : prefix.toCharArray()) {
            currentNode = currentNode.get(c);
            if (currentNode == null) {
                return 0;
            }
        }
        return currentNode.getEntryCount();
    }
}
