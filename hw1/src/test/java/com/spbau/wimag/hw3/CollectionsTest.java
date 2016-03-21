package com.spbau.wimag.hw3;

import org.junit.Test;
import org.omg.CORBA.INTERNAL;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Mark on 21.03.2016.
 */
public class CollectionsTest {

    private final List<Integer> testList = Arrays.asList(1, 2, 3, 4, 5);

    private final Predicate<Integer> isOdd = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer arg) {
            return (arg % 2) == 1;
        }
    };

    private final Function1<Integer, Integer> func = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer arg) {
            return 6 - arg;
        }
    };

    private final Predicate<Integer> less4 = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer arg) {
            return arg < 4;
        }
    };

    private final Function2<Integer, Integer, Integer> div = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer arg1, Integer arg2) {
            return arg1 / arg2;
        }
    };

    @Test
    public void testMap() throws Exception {
        final List<Integer> revList = Arrays.asList(5, 4, 3, 2, 1);
        assertEquals(revList, Collections.map(func, testList));
    }

    @Test
    public void testFilter() throws Exception {
        final List<Integer> oddList = Arrays.asList(1, 3, 5);
        assertEquals(oddList, Collections.filter(isOdd, testList));
    }

    @Test
    public void testTakeWhile() throws Exception {
        final List<Integer> list = Arrays.asList(1, 2, 3);
        assertEquals(list, Collections.takeWhile(less4, testList));
    }

    @Test
    public void testTakeUnless() throws Exception {
        final List<Integer> list = Arrays.asList(1, 2, 3);
        assertEquals(list, Collections.takeUnless(less4.not(), testList));
    }

    @Test
    public void testFoldr() throws Exception {
        List<Integer> test = Arrays.asList(8, 12, 24, 4);
        assertEquals(8, (int) Collections.foldr(div, 2, test));
    }

    @Test
    public void testFoldl() throws Exception {
        List<Integer> test = Arrays.asList(4, 2, 4);
        assertEquals(2, (int) Collections.foldl(div, 64, test));
    }
}