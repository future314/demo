package consumer.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import util.ConsumerUtil;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 控制或关闭消费
 *
 * @author LR
 */
public class PauseAndResumeDemo {
    public static final String topic = "topic-demo";
    public static final AtomicBoolean isRunning = new AtomicBoolean(true);
    public static final int MIN_BATCH_SIZE = 200;

    public static void main(String[] args) {
        Properties properties = ConsumerUtil.initConfig();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(topic));
        ConsumerRecords<String, String> partitionRecords = consumer.poll(Duration.ofMillis(1000));
        Set<TopicPartition> topicPartitionSet = new HashSet<>();
        for (TopicPartition topicPartition : partitionRecords.partitions()) {
            topicPartitionSet.add(topicPartition);
            break;
        }
        // 暂停分区集合
        consumer.pause(topicPartitionSet);
        int count = 0;
        try {
            while (isRunning.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                count += records.count();
                System.out.println("records count is: " + count);
                // 恢复消费
                consumer.resume(topicPartitionSet);
                break;
            }
            Thread.sleep(10000);
            while (isRunning.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                count += records.count();
                System.out.println("records count is: " + count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.commitSync();
            consumer.unsubscribe();
            consumer.close();
        }
    }
}
