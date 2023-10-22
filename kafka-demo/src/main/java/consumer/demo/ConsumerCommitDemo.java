package consumer.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.record.Record;
import util.ConsumerUtil;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 消费位移提交
 *
 * @author LR
 */
public class ConsumerCommitDemo {
    public static final String topic = "topic-demo";
    public static final AtomicBoolean isRunning = new AtomicBoolean(true);
    public static final int MIN_BATCH_SIZE = 200;

    public static void main(String[] args) {
        Properties properties = ConsumerUtil.initConfig();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(topic));
        try {
            while (isRunning.get()) {
                //commitMessagePartitions(consumer);
                //commitMessageEverOffset(consumer);
                commitMessageEverPartitions(consumer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.unsubscribe();
            consumer.close();
        }
    }

    public static void handleRecords(List<ConsumerRecord<String, String>> records) {
        System.out.println("handle " + records.size());
    }

    public static void handleRecord(ConsumerRecord<String, String> record) {
        System.out.println("handle :" + record.toString());
    }

    /**
     * 提交指定分区的位移
     * 每消费一条消息就同步提交一次位移
     *
     * @param consumer
     */
    public static void commitMessageEverOffset(KafkaConsumer<String, String> consumer) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
        for (ConsumerRecord<String, String> record : records) {
            long offset = record.offset();
            handleRecord(record);
            TopicPartition partition = new TopicPartition(record.topic(), record.partition());
            consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(offset + 1)));
        }
    }

    /**
     * 提交消费位移的频率和拉取批次消息、处理批次消息的频率是一样
     * 将拉取到的消息存入缓存 recordBuffer，等到积累到足够多的时候，也就是示例中大于等于MIN_BATCH_SIZE时，再做相应的批量处理，之后再做同步批量提交
     *
     * @param consumer
     */
    public static void commitMessageEverPoll(KafkaConsumer<String, String> consumer) {
        List<ConsumerRecord<String, String>> recordBuffer = new ArrayList<>();
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
        records.forEach(record -> recordBuffer.add(record));
        if (recordBuffer.size() >= MIN_BATCH_SIZE) {
            handleRecords(recordBuffer);
            consumer.commitSync();
            recordBuffer.clear();
        }
    }

    /**
     * 按分区粒度同步提交消费位移
     *
     * @param consumer
     */
    public static void commitMessageEverPartitions(KafkaConsumer<String, String> consumer) {
        List<ConsumerRecord<String, String>> recordBuffer = new ArrayList<>();
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
        for (TopicPartition partition : records.partitions()) {
            List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
            recordBuffer.addAll(partitionRecords);
            handleRecords(recordBuffer);
            long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
            consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
        }

    }
}
