import java.util.Queue;

/**
 * Sort of BlockingQUeue
 * Created by Mark on 01.05.2016.
 */
public class ConcurrentQueue<T> {
    private final Queue<T> queue = new java.util.LinkedList<>();
    private volatile boolean terminated = false;

    /**
     * get first element. Blocks Thread. LinkedList poll is
     * thread safe on empty queue btw
     * @return Element at the head of queue
     * @throws InterruptedException
     */
    public synchronized T poll() throws InterruptedException {
        T value;
        while (queue.isEmpty()) {
            wait();
        }
        value = queue.poll();
        return value;
    }

    /**
     * add element to Queue
     * @param value - element tu add to the queue
     */
    public synchronized void push(T value) {
        queue.add(value);
        notifyAll();
    }

}
