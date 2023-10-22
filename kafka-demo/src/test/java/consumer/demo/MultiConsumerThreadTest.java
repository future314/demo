package consumer.demo;

import org.junit.Test;

public class MultiConsumerThreadTest {
    @Test
    public void startMultiConsumerThread() throws InterruptedException {
        MultiConsumerThreadDemo.startMultiConsumerThread(4);
    }

    @Test
    public void startMultiHandleThread() {
        MultiConsumerThreadDemo.startMultiHandleThread();
    }
}
