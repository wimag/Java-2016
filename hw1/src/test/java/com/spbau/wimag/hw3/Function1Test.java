package com.spbau.wimag.hw3;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Mark on 21.03.2016.
 */
public class Function1Test {

    private final static Function1<Integer, Integer> TIMES_TWO = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer arg) {
            return 2 * arg;
        }
    };

    private final static Function1<Integer, Integer> ADD = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer arg) {
            return arg + 1;
        }
    };

    private final static Function1<Object, Integer> PARSE_AS_STRING = new Function1<Object, Integer>() {
        @Override
        public Integer apply(Object arg) {
            return Integer.parseInt(arg.toString());
        }
    };


    private static class Base{
        private final int x;

        public Base(int x) {
            this.x = x;
        }

        public int getX() {
            return x;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Base base = (Base) o;

            return x == base.x;

        }

        @Override
        public int hashCode() {
            return x;
        }
    }

    private static class Derived extends Base {
        public Derived(int x) {
            super(x);
        }
    }

    private final static Function1<Derived, Derived> WILDCARD_TIMES_TWO = new Function1<Derived, Derived>() {
        @Override
        public Derived apply(Derived arg) {
            return new Derived(2 * arg.getX());
        }
    };

    private final static Function1<Base, Base> WILDCARD_ADD = new Function1<Base, Base>() {
        @Override
        public Base apply(Base arg) {
            return new Base(arg.getX() + 1);
        }
    };


    @Test
    public void testApply() throws Exception {
        assertEquals(3, (int) ADD.apply(2));
        assertEquals(4, (int) TIMES_TWO.apply(2));
        assertEquals(4, (int) PARSE_AS_STRING.apply(4));

        Derived two = new Derived(2);
        Base three = new Base(3);
        Derived four = new Derived(4);
        assertEquals(three, WILDCARD_ADD.apply(two));
        assertEquals(four, WILDCARD_TIMES_TWO.apply(two));

    }

    @Test
    public void testCompose() throws Exception {
        assertEquals(6, (int) (ADD.compose(TIMES_TWO)).apply(2));
        assertEquals(5, (int) (TIMES_TWO.compose(ADD)).apply(2));

        Derived two = new Derived(2);
        Base five = new Base(5);

        assertEquals(five,(WILDCARD_TIMES_TWO.compose(WILDCARD_ADD)).apply(two));


    }
}