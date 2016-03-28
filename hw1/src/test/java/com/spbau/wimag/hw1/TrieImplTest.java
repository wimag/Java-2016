package com.spbau.wimag.hw1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TrieImplTest {

    @org.junit.Test
    public void testAdd() throws Exception {
        Trie trie = new TrieImpl();

        assertTrue(trie.add("abc"));
        assertTrue(trie.add("def"));
        assertTrue(trie.add("abacabadabacaba"));
        assertFalse(trie.add("abacabadabacaba"));
        assertTrue(trie.add("abacabadabacaba1"));
        assertTrue(trie.add("abacabadabacaba2"));
        assertTrue(trie.add("abacabadabacaba3"));
        assertTrue(trie.add("abacabadabacaba4"));


        assertEquals(7, trie.size());

        assertTrue(trie.contains("abc"));
        assertTrue(trie.contains("def"));
        assertFalse(trie.contains("a"));
        assertFalse(trie.contains("de"));
        assertFalse(trie.contains(""));
        assertFalse(trie.contains("abcdef"));

        trie.add("ad");

        assertEquals(8, trie.size());
        assertEquals(7, trie.howManyStartsWithPrefix("a"));
        assertEquals(1, trie.howManyStartsWithPrefix("d"));

        assertFalse(trie.contains("a"));
        assertFalse(trie.contains("ab"));

        trie.add("");

        assertEquals(9, trie.size());
        assertEquals(9, trie.howManyStartsWithPrefix(""));
        assertTrue(trie.contains(""));
    }

    @org.junit.Test
    public void testContains() throws Exception {
        Trie trie = new TrieImpl();

        trie.add("abc");
        trie.add("def");
        trie.add("abcdef");
        trie.add("qwe");
        trie.add("rty");
        trie.add("qwerty");
        trie.add("asdfghjk");

        assertTrue(trie.contains("abcdef"));

        trie.remove("abcdef");
        assertFalse(trie.contains("abcdef"));
        assertTrue(trie.contains("abc"));
        trie.remove("qwe");
        assertTrue(trie.contains("qwerty"));
        assertFalse(trie.contains("qwe"));
    }

    @org.junit.Test
    public void testRemove() throws Exception {
        Trie trie = new TrieImpl();

        trie.add("abc");
        trie.add("def");

        assertTrue(trie.remove("abc"));
        assertEquals(1, trie.size());

        trie.add("");
        trie.add("ad");
        trie.add("af");
        trie.add("ad");
        trie.add("af");
        trie.add("abacabadabacaba");
        assertEquals(5, trie.howManyStartsWithPrefix(""));
        assertEquals(5, trie.size());
        assertEquals(3, trie.howManyStartsWithPrefix("a"));

        assertTrue(trie.remove(""));
        assertFalse(trie.remove(""));
        assertFalse(trie.remove("abc"));
        assertTrue(trie.remove("ad"));

        assertEquals(2, trie.howManyStartsWithPrefix("a"));
    }
}