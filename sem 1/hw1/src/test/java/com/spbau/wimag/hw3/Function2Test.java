package com.spbau.wimag.hw3;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Mark on 21.03.2016.
 */
public class Function2Test {

    private static final Function2<Integer, Integer, Integer> SUB = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer arg1, Integer arg2) {
            return arg1 - arg2;
        }
    };

    private static final Function2<Integer, Integer, Integer> ADD = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer arg1, Integer arg2) {
            return arg1 + arg2;
        }
    };

    private static final Function2<Integer, Integer, Integer> MUL = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer arg1, Integer arg2) {
            return arg1 * arg2;
        }
    };

    private static final Function1<Integer, Integer> NEG = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer arg) {
            return -arg;
        }
    };

    private static final Function1<Object, String> TO_STRING = new Function1<Object, String>() {
        @Override
        public String apply(Object arg) {
            return arg.toString();
        }
    };

    @Test
    public void testApply() throws Exception {
        assertEquals(-1, (int) SUB.apply(2, 3));
        assertEquals(5, (int) ADD.apply(2, 3));
        assertEquals(6, (int) MUL.apply(2, 3));
    }

    @Test
    public void testCompose() throws Exception {
        assertEquals(6, (int) (MUL.compose(NEG)).apply(2, -3));
        assertEquals("100", (MUL.compose(TO_STRING)).apply(10, 10));
    }

    @Test
    public void testBind1() throws Exception {
        assertEquals(-7, (int) (SUB.bind1(-1)).apply(6));
        assertEquals(-6, (int) (MUL.bind1(-1)).apply(6));
    }

    @Test
    public void testBind2() throws Exception {
        assertEquals(5, (int) (ADD.bind2(-1)).apply(6));
        assertEquals(7, (int) (SUB.bind2(-1)).apply(6));
    }

    @Test
    public void testCurry() throws Exception {
        Function1<Integer, Function1<Integer, Integer>> currySub = SUB.curry();
        assertEquals((int) SUB.apply(123, 456), (int) currySub.apply(123).apply(456));
    }
}