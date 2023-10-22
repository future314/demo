package consumer.demo;

import kafkaAdmin.demo.KafkaAdminDemo;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

public class KafkaAdminDemoTest {
    @Test
    public void createTopic() throws ExecutionException, InterruptedException {
        KafkaAdminDemo.createTopic();
    }

    @Test
    public void describeTopicConfig() throws ExecutionException, InterruptedException {
        KafkaAdminDemo.describeTopicConfig();
    }
}
