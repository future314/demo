package study.thread;

import java.util.concurrent.*;

/**
 * @author LR
 */
public class InterruptThread {
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void runThread() {
        Future<String> future = executor.submit(() -> {
            while (true) {
                try {
                    System.out.println("running...");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(Thread.currentThread().getName() + "isInterrupted");
                    e.printStackTrace();
                    return "result isInterrupted";
                }
            }
        });
        try {
            String result = future.get(2, TimeUnit.SECONDS);
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();

            // 取消任务，中断线程
            future.cancel(true);
        }
    }

    public static void main(String[] args) {
        runThread();
    }
}
