package com.spbau.wimag.hw3;

import com.google.common.collect.ImmutableMap;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        String TEST_FILE1 = "src\\test\\java\\com\\spbau\\wimag\\hw3\\SecondPartTasksFile1.txt";
        String TEST_FILE2 = "src\\test\\java\\com\\spbau\\wimag\\hw3\\SecondPartTasksFile2.txt";
        List<String> testList = Arrays.asList(TEST_FILE1, TEST_FILE2);
        assertEquals(Arrays.asList("qwerty", "qwertyuiop"), SecondPartTasks.findQuotes(testList, "rty"));
        assertEquals(Collections.singletonList("Utamur civibus adversarium mea ea, est et" +
                " utamur scripta. Est an laudem saperet inimicus."), SecondPartTasks.findQuotes(testList, "est "));

    }

    @Test
    public void testPiDividedBy4() {
        double EPS = 0.01;
        assertTrue(Math.abs(Math.PI / 4 - SecondPartTasks.piDividedBy4()) < EPS);
    }

    @Test
    public void testFindPrinter() {
        Map<String, List<String>> authors = ImmutableMap.of("a", Arrays.asList("1234567", "12345678"),
                "b", Arrays.asList("1234567", "1234567", "89"));
        assertEquals("b", SecondPartTasks.findPrinter(authors));
    }

    @Test
    public void testCalculateGlobalOrder() {
        List<Map<String, Integer>> orders = Arrays.asList(ImmutableMap.of("a", 1, "b", 2, "c", 13, "d", 27),
                ImmutableMap.of("c", 17, "d", 23, "e", 123));
        Map<String, Integer> res = null;
        assertEquals(ImmutableMap.of("a", 1, "b", 2, "c", 30, "d", 50, "e", 123),
                SecondPartTasks.calculateGlobalOrder(orders));

    }
}