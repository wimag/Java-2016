package com.spbau.wimag.hw3;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Mark on 21.03.2016.
 */
public class CollectionsTest {

    private static final List<Integer> TEST_LIST = Arrays.asList(1, 2, 3, 4, 5);

    private static final Predicate<Integer> IS_ODD = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer arg) {
            return (arg % 2) == 1;
        }
    };

    private static final Function1<Integer, Integer> INTEGER_FUNCTION_1 = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer arg) {
            return 6 - arg;
        }
    };

    private static final Predicate<Integer> LESS_THEN_4 = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer arg) {
            return arg < 4;
        }
    };

    private static final Function2<Integer, Integer, Integer> DIV = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer arg1, Integer arg2) {
            return arg1 / arg2;
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


    private static final List<Derived> WILDCARD_TEST_LIST = Arrays.asList(new Derived(1),
            new Derived(2), new Derived(3), new Derived(4), new Derived(5));

    private static final Predicate<Base> WILDCARD_IS_ODD = new Predicate<Base>() {
        @Override
        public Boolean apply(Base arg) {
            return (arg.getX() % 2) == 1;
        }
    };

    private static final Function1<Base, Integer> WILDCARD_INTEGER_FUNCTION_1 = new Function1<Base, Integer>() {
        @Override
        public Integer apply(Base arg) {
            return 6 - arg.getX();
        }
    };

    private static final Predicate<Base> WILDCARD_LESS_THEN_4 = new Predicate<Base>() {
        @Override
        public Boolean apply(Base arg) {
            return arg.getX() < 4;
        }
    };

    private static final Function2<Base, Integer, Integer> WILDCARD_DIV_1 = new Function2<Base, Integer, Integer>() {
        @Override
        public Integer apply(Base arg1, Integer arg2) {
            return arg1.getX() / arg2;
        }
    };

    private static final Function2<Integer, Base, Integer> WILDCARD_DIV_2 = new Function2<Integer, Base, Integer>() {
        @Override
        public Integer apply(Integer arg1, Base arg2) {
            return arg1/ arg2.getX();
        }
    };

    private static List<Derived> getWildcardList(List<Integer> list){
        List<Derived> res = new ArrayList<>();
        for(Integer x: list){
            res.add(new Derived(x));
        }
        return res;
    }


    @Test
    public void testMap() throws Exception {
        final List<Integer> revList = Arrays.asList(5, 4, 3, 2, 1);
        assertEquals(revList, Collections.map(INTEGER_FUNCTION_1, TEST_LIST));
        assertEquals(revList, Collections.map(WILDCARD_INTEGER_FUNCTION_1, WILDCARD_TEST_LIST));
    }

    @Test
    public void testFilter() throws Exception {
        final List<Integer> oddList = Arrays.asList(1, 3, 5);
        assertEquals(oddList, Collections.filter(IS_ODD, TEST_LIST));
        final List<Derived> wildcardOddList = getWildcardList(oddList);
        assertEquals(wildcardOddList, Collections.filter(WILDCARD_IS_ODD, WILDCARD_TEST_LIST));
    }

    @Test
    public void testTakeWhile() throws Exception {
        final List<Integer> list = Arrays.asList(1, 2, 3);
        assertEquals(list, Collections.takeWhile(LESS_THEN_4, TEST_LIST));
        final List<Derived> wildcardList = getWildcardList(list);
        assertEquals(wildcardList, Collections.takeWhile(WILDCARD_LESS_THEN_4, WILDCARD_TEST_LIST));
    }

    @Test
    public void testTakeUnless() throws Exception {
        final List<Integer> list = Arrays.asList(1, 2, 3);
        assertEquals(list, Collections.takeUnless(LESS_THEN_4.not(), TEST_LIST));

        final List<Derived> wildcardList = getWildcardList(list);
        assertEquals(wildcardList, Collections.takeUnless(WILDCARD_LESS_THEN_4.not(), WILDCARD_TEST_LIST));
    }

    @Test
    public void testFoldr() throws Exception {
        List<Integer> test = Arrays.asList(8, 12, 24, 4);
        assertEquals(8, (int) Collections.foldr(DIV, 2, test));

        final List<Derived> wildcardList = getWildcardList(test);
        assertEquals(8, (int) Collections.foldr(WILDCARD_DIV_1, 2, wildcardList));

    }

    @Test
    public void testFoldl() throws Exception {
        List<Integer> test = Arrays.asList(4, 2, 4);
        assertEquals(2, (int) Collections.foldl(DIV, 64, test));

        final List<Derived> wildcardList = getWildcardList(Arrays.asList(8, 12, 24, 4));
        assertEquals(0, (int) Collections.foldl(WILDCARD_DIV_2, 2, wildcardList));
    }
}