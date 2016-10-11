/**
 * Created by Mark on 01.05.2016.
 */
public class Worker extends Thread {
    private final ConcurrentQueue<LightFuture> queue;
    private final String name;

    public Worker(ConcurrentQueue<LightFuture> queue, String name) {
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            LightFuture task;
            try {
                task = queue.poll();
            } catch (InterruptedException e) {
                break;
            }
            if (task == null) {
                continue;
            }
            if (!task.isWaiting()) {
                queue.push(task);
                continue;
            }
            task.compute();
        }
    }
}
