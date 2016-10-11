package com.spbau.wimag.hw3;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Mark on 21.03.2016.
 */
public class PredicateTest {

    private static final Predicate<Object> BOT = new Predicate<Object>() {
        @Override
        public Boolean apply(Object arg) {
            throw new UnsupportedOperationException("Woops, BOT reached");
        }
    };

    public static final Predicate<Integer> ODD = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer arg) {
            return (arg % 2) == 1;
        }
    };

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
        assertTrue(Predicate.ALWAYS_TRUE.or(BOT).apply(false));

        assertTrue(ODD.or(Predicate.ALWAYS_FALSE).apply(3));
    }

    @Test
    public void testAnd() throws Exception {
        assertTrue(Predicate.ALWAYS_FALSE.and(Predicate.ALWAYS_FALSE).not().apply(true));
        assertFalse(Predicate.ALWAYS_FALSE.and(Predicate.ALWAYS_FALSE).apply(true));
        assertFalse(Predicate.ALWAYS_FALSE.and(Predicate.ALWAYS_TRUE).apply(true));
        assertTrue(Predicate.ALWAYS_TRUE.and(Predicate.ALWAYS_TRUE).apply(true));
        assertFalse(Predicate.ALWAYS_FALSE.and(BOT).apply(true));

        assertTrue(ODD.and(Predicate.ALWAYS_TRUE).apply(3));
    }
}