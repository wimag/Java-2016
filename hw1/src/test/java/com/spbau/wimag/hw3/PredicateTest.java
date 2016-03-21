package com.spbau.wimag.hw3;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Mark on 21.03.2016.
 */
public class PredicateTest {

    @Test
    public void testNot() throws Exception {
        assertTrue(Predicate.ALWAYS_FALSE.not().apply(true));
        assertTrue(Predicate.ALWAYS_FALSE.not().apply(false));
        assertFalse(Predicate.ALWAYS_TRUE.not().apply(true));
        assertFalse(Predicate.ALWAYS_TRUE.not().apply(false));
    }

    @Test
    public void testOr() throws Exception {
        assertTrue(Predicate.ALWAYS_TRUE.or(Predicate.ALWAYS_FALSE).apply(true));
        assertTrue(Predicate.ALWAYS_TRUE.or(Predicate.ALWAYS_FALSE).apply(false));
        assertFalse(Predicate.ALWAYS_FALSE.or(Predicate.ALWAYS_FALSE).apply(true));
    }

    @Test
    public void testAnd() throws Exception {
        assertTrue(Predicate.ALWAYS_FALSE.and(Predicate.ALWAYS_FALSE).not().apply(true));
        assertFalse(Predicate.ALWAYS_FALSE.and(Predicate.ALWAYS_FALSE).apply(true));
        assertFalse(Predicate.ALWAYS_FALSE.and(Predicate.ALWAYS_TRUE).apply(true));
        assertTrue(Predicate.ALWAYS_TRUE.and(Predicate.ALWAYS_TRUE).apply(true));
    }
}