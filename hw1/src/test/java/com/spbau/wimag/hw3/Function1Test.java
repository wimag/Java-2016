package com.spbau.wimag.hw3;

import com.spbau.wimag.hw1.StreamSerializable;
import org.junit.Test;
import org.omg.PortableInterceptor.Interceptor;

import static org.junit.Assert.*;

/**
 * Created by Mark on 21.03.2016.
 */
public class Function1Test {

    private Function1<Integer, Integer> x2 = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer arg) {
            return 2 * arg;
        }
    };

    private Function1<Integer, Integer> add1 = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer arg) {
            return arg + 1;
        }
    };

    private Function1<Object, Integer> parseAsString = new Function1<Object, Integer>() {
        @Override
        public Integer apply(Object arg) {
            return Integer.parseInt(arg.toString());
        }
    };


    @Test
    public void testApply() throws Exception {
        assertEquals(3, (int) add1.apply(2));
        assertEquals(4, (int) x2.apply(2));
        assertEquals(4, (int) parseAsString.apply(4));
    }

    @Test
    public void testCompose() throws Exception {
        assertEquals(6, (int) (add1.compose(x2)).apply(2));
        assertEquals(5, (int) (x2.compose(add1)).apply(2));
    }
}