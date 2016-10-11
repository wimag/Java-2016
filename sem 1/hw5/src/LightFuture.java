import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * Sort of FuttureTask from concurrent
 * (not relly). Main object type for
 * interacting with ThreadPoolImpl
 * Created by Mark on 01.05.2016.
 */
public class LightFuture<T> {
    private volatile T result = null;
    private volatile ResultStatus status = ResultStatus.Pending;
    private final ThreadPoolImpl threadPool;
    private final LightFuture parent;
    private final Supplier<T> supplier;

    public LightFuture(ThreadPoolImpl threadPool, Supplier<T> supplier) {
        this.threadPool = threadPool;
        this.parent = null;
        this.supplier = supplier;
    }

    public LightFuture(ThreadPoolImpl threadPool, Supplier<T> supplier, LightFuture parent) {
        this.threadPool = threadPool;
        this.parent = parent;
        this.supplier = supplier;
    }

    /**
     * Get check if computation ended (Doesn't
     * matter if ended with Error or Success)
     * @return
     */
    public boolean isReady() {
        return (status == ResultStatus.Error || status == ResultStatus.OK);
    }

    /**
     * Check, if task is ready to be executed.
     * This computations are required for async
     * computations of {@link #thenApply(Function)}
     * @return true - if can be executed now
     */
    public synchronized boolean isWaiting() {
        if (status == ResultStatus.Pending) {
            if (parent == null || parent.isReady()) {
                status = ResultStatus.Waiting;
            }
        }
        return status == ResultStatus.Waiting;
    }

    /**
     * Applies function after the end of computations.
     * LightFuture is created and enqueued with this call
     * but invoked only after parent computations are
     * completed
     * @param function - function to apply
     * @param <R> - return type
     * @return
     */
    public <R> LightFuture<R> thenApply(Function<? super T, R> function) {
        return threadPool.submit(() -> function.apply(this.get()), this);
    }

    /**
     * Get current Task status. See {@link LightFuture.ResultStatus}
     * @return
     */
    public ResultStatus getStatus() {
        return status;
    }

    /**
     *
     * @return
     * @throws LightExecutionException
     */
    public synchronized T get() throws LightExecutionException {
        while (!isReady()) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        if (status == ResultStatus.Error) {
            throw new LightExecutionException();
        }
        return result;
    }

    /**
     * package level function - invoked by
     * workers. Evaluates supplier
     */
    synchronized void compute() {
        try {
            result = supplier.get();
            status = ResultStatus.OK;
        } catch (Exception e) {
            status = ResultStatus.Error;
        } finally {
            notifyAll();
        }
    }

    /**
     * Current run status
     * Pending = waiting for parent task to end
     * Waiting = Ready to be executed
     * OK = computations ended with success
     * Error = Exception raised
     */
    public enum ResultStatus {
        Pending,
        Waiting,
        OK,
        Error
    }

}
