import java.util.ArrayList;
import java.util.Queue;
import java.util.function.Supplier;

/**
 * Imitation of FixedThreadPoolExecutor
 * Created by Mark on 01.05.2016.
 */
public class ThreadPoolImpl {
    private final ConcurrentQueue<LightFuture> queue = new ConcurrentQueue();

    private final Thread[] workers;

    public ThreadPoolImpl() {
        this(10);
    }

    public ThreadPoolImpl(int n) {
        workers = new Thread[n];
        initThreads(n);
    }

    /**
     * Add a task for execution. Currently all tasks
     * sould be in supplier format
     * @param supplier - function to be invoked
     * @param <T> - return value of @param supplier
     * @return {@link LightFuture}
     */
    public <T> LightFuture<T> submit(Supplier<T> supplier) {
        LightFuture<T> future = new LightFuture<>(this, supplier);
        queue.push(future);
        return future;
    }

    /**
     * Add a task with a dependency
     * @param supplier - function to be invoked
     * @param condition - Parent task. It must complete
     *                  it's computations before this task is run
     * @param <T> - return value of @param supplier
     * @return {@link LightFuture}
     */
    public <T> LightFuture<T> submit(Supplier<T> supplier, LightFuture<?> condition) {
        LightFuture<T> future = new LightFuture<>(this, supplier, condition);
        queue.push(future);
        return future;
    }

    /**
     * End all threads. Current computations
     * will be finished
     */
    public void shutdown() {
        for (Thread worker : workers) {
            worker.interrupt();
            queue.push(null);
        }
    }

    /**
     * initialize n Worker Threads
     * @param n
     */
    private void initThreads(int n) {
        for (int i = 0; i < n; i++) {
            workers[i] = new Worker(queue, "Worker " + i);
            workers[i].start();
        }
    }

}
