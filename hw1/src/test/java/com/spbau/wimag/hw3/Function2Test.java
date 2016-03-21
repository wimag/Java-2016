package com.spbau.wimag.hw3;

import org.junit.Test;
import org.omg.PortableInterceptor.Interceptor;

import static org.junit.Assert.*;

/**
 * Created by Mark on 21.03.2016.
 */
public class Function2Test {

    private final Function2<Integer, Integer, Integer> sub = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer arg1, Integer arg2) {
            return arg1 - arg2;
        }
    };

    private final Function2<Integer, Integer, Integer> add = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer arg1, Integer arg2) {
            return arg1 + arg2;
        }
    };

    private final Function2<Integer, Integer, Integer> mul = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer arg1, Integer arg2) {
            return arg1 * arg2;
        }
    };

    private final Function1<Integer, Integer> neg = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer arg) {
            return -arg;
        }
    };

    private final Function1<Object, String> toString = new Function1<Object, String>() {
        @Override
        public String apply(Object arg) {
            return arg.toString();
        }
    };

    @Test
    public void testApply() throws Exception {
        assertEquals(-1, (int) sub.apply(2, 3));
        assertEquals(5, (int) add.apply(2, 3));
        assertEquals(6, (int) mul.apply(2, 3));
    }

    @Test
    public void testCompose() throws Exception {
        assertEquals(6, (int) (mul.compose(neg)).apply(2, -3));
        assertEquals("100", (mul.compose(toString)).apply(10, 10));
    }

    @Test
    public void testBind1() throws Exception {
        assertEquals(-7, (int) (sub.bind1(-1)).apply(6));
        assertEquals(-6, (int) (mul.bind1(-1)).apply(6));
    }

    @Test
    public void testBind2() throws Exception {
        assertEquals(5, (int) (add.bind2(-1)).apply(6));
        assertEquals(7, (int) (sub.bind2(-1)).apply(6));
    }

    @Test
    public void testCurry() throws Exception {
        Function1<Integer, Function1<Integer, Integer>> currySub = sub.curry();
        assertEquals((int) sub.apply(123, 456), (int) currySub.apply(123).apply(456));
    }
}