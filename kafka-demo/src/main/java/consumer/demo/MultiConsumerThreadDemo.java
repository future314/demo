package consumer.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import util.ConsumerUtil;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * @author LR
 * <p>
 * 多线程消费
 */
public class MultiConsumerThreadDemo {
    private static ExecutorService consumerThreadPool = Executors.newCachedThreadPool();
    private static ThreadPoolExecutor handleThreadPool = new ThreadPoolExecutor(3, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
    public static final String topic = "topic-demo";

    public static void startMultiConsumerThread(int threadNum) throws InterruptedException {

        for (int i = 0; i < threadNum; i++) {
            consumerThreadPool.execute(() -> {
                Properties properties = ConsumerUtil.initConfig();
                KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
                kafkaConsumer.subscribe(Collections.singletonList(topic));
                while (true) {
                    ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));
                    for (ConsumerRecord<String, String> record : records) {
                        System.out.println(Thread.currentThread() + "=" + record.value());
                    }
                }
            });
        }
        consumerThreadPool.awaitTermination(10, TimeUnit.MINUTES);
    }

    public static void startMultiHandleThread() {
        Properties properties = ConsumerUtil.initConfig();
        properties.put("enable.auto.commit", false);
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));
            Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
            handleThreadPool.execute(() -> {
                for (ConsumerRecord<String, String> record : records) {
                    offsets.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset()+1));
                    System.out.println(Thread.currentThread() + "=" + record.value());
                }
            });
            kafkaConsumer.commitSync(
                    offsets
            );
            //kafkaConsumer.commitSync();
        }
    }
}
