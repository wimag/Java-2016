import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * /**
 * Created by Mark on 01.05.2016.
 */
public class ThreadPoolImplTest {
    private final int BASE_TASK_COUNT = 20;


    @org.junit.Test
    public void testPoolSize() throws Exception {
        ThreadPoolImpl threadPool = new ThreadPoolImpl(4);
        Set<Long> threadIDs = new HashSet<>();
        ArrayList<LightFuture<Long>> futures = new ArrayList<>();
        for (int i = 0; i < BASE_TASK_COUNT; i++) {
            futures.add(threadPool.submit(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                return Thread.currentThread().getId();
            }));
        }
        for (int i = 0; i < BASE_TASK_COUNT; i++) {
            threadIDs.add(futures.get(i).get());
        }
        threadPool.shutdown();
        assertEquals(threadIDs.size(), 4);
    }

    @org.junit.Test
    public void testThenApply() {
        ThreadPoolImpl threadPool = new ThreadPoolImpl(4);
        ArrayList<LightFuture<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < BASE_TASK_COUNT; i++) {
            final int finalI = i;
            futures.add(threadPool.submit(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                return finalI;
            }));
        }
        for (int i = 0; i < BASE_TASK_COUNT; i++) {
            futures.add(futures.get(i).thenApply((Integer x) -> 2 * x));
        }
        int firstHalf = 0;
        int secondHalf = 0;
        for (int i = 0; i < BASE_TASK_COUNT; i++) {
            firstHalf += futures.get(i).get();
            assertTrue(futures.get(i).isReady());
            secondHalf += futures.get(i + BASE_TASK_COUNT).get();
            assertTrue(futures.get(i + BASE_TASK_COUNT).isReady());
        }
        assertEquals(firstHalf * 2, secondHalf);
        threadPool.shutdown();

    }

    @org.junit.Test(expected = LightExecutionException.class)
    public void testException() {
        ThreadPoolImpl threadPool = new ThreadPoolImpl(1);
        LightFuture<Integer> future = threadPool.submit(() -> {
            throw new RuntimeException();
        });
        future.get();
        threadPool.shutdown();
    }

    @org.junit.Test(timeout = 30000)
    public void testAsync() {
        ThreadPoolImpl threadPool = new ThreadPoolImpl(2);
        LightFuture<Integer> future = threadPool.submit(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        });

        ArrayList<LightFuture<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < BASE_TASK_COUNT; i++) {
            futures.add(future.thenApply((Integer x) -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return x;
            }));
            futures.add(threadPool.submit(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 1;
            }));
        }


        for (LightFuture<Integer> future1 : futures) {
            future1.get();
        }
    }
}