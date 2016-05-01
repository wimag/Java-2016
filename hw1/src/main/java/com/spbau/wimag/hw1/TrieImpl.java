package com.spbau.wimag.hw1;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * Created by Mark on 18.02.2016.
 */
public class TrieImpl implements Trie{
    private Node root = new Node();

    private int size;

    /**
     * {@inheritDoc}
     *
     * @param element
     * @return
     */
    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }
        Node currentNode = root;
        for (char c : element.toCharArray()) {
            currentNode = currentNode.insert(c);
        }
        currentNode.setWordEndStatus(true);
        size++;
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param element
     * @return
     */
    @Override
    public boolean contains(String element) {
        Node currentNode = root;
        for (char c : element.toCharArray()) {
            currentNode = currentNode.get(c);
            if (currentNode == null) {
                return false;
            }
        }
        return currentNode.isWordEnd();
    }

    /**
     * {@inheritDoc}
     *
     * @param element
     * @return
     */
    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }
        Node currentNode = root;
        for (char c : element.toCharArray()) {
            currentNode = currentNode.remove(c);
            if (currentNode == null) {
                return false;
            }
        }
        size--;
        currentNode.setWordEndStatus(false);

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * {@inheritDoc}
     *
     * @param prefix
     * @return
     */
    @Override
    public int howManyStartsWithPrefix(String prefix) {
        if (prefix.isEmpty()) {
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

    @Override
    public void serialize(OutputStream out) throws IOException {
        PrintWriter writer = new PrintWriter(out);
        Queue<Node> queue = new ArrayDeque<>();
        writer.println(size);
        queue.offer(root);
        while(!queue.isEmpty()){
            Node cur = queue.poll();
            writer.printf("%d ", cur.isWordEnd() ? 1 : 0);
            writer.println(cur.getChildrenRecords().size());
            for(NodeRecord nr: cur.getChildrenRecords()) {
                writer.printf("%c %d ", nr.getKey(), nr.getValue().getEntryCount());
                queue.offer(nr.getValue());
            }
        }
        writer.close();
    }

    @Override
    public void deserialize(InputStream in) throws IOException {
        Reader reader = new Reader(in);
        size = reader.nextInt();
        root = new Node();
        Queue<Node> queue = new ArrayDeque<>();
        queue.offer(root);
        while(!queue.isEmpty()){
            Node cur = queue.poll();
            cur.setWordEndStatus(reader.nextInt() == 1);
            int m = reader.nextInt();
            for(int i = 0; i < m; i++){
                char c = reader.nextChar();
                int n = reader.nextInt();
                queue.offer(cur.insert(c, n));
            }
        }
    }


    private static class Reader{
        BufferedReader reader;
        StringTokenizer tokenizer;

        public Reader(InputStream in){
            reader = new BufferedReader(new InputStreamReader(in));
            tokenizer = new StringTokenizer("");
        }

        public String next() throws IOException {
            while(!tokenizer.hasMoreTokens()){
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        public char nextChar() throws IOException {
            return next().charAt(0);
        }
    }
}
