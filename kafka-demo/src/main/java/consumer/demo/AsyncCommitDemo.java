package consumer.demo;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import util.ConsumerUtil;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 异步提交
 *
 * @author LR
 */
public class AsyncCommitDemo {
    public static final String topic = "topic-demo";
    public static final AtomicBoolean isRunning = new AtomicBoolean(true);
    public static final int MIN_BATCH_SIZE = 200;

    public static void main(String[] args) {
        Properties properties = ConsumerUtil.initConfig();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(topic));
        Callback callback = new Callback();
        try {
            while (isRunning.get()) {
                //AsynCommitMessageEverOffset(consumer,callback);
                //AsynCommitMessageEverPoll(consumer,callback);
                AsynCommitMessageEverPartitions(consumer, callback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.commitSync();
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
     * 每消费一条消息就异步提交一次位移
     *
     * @param consumer
     */
    public static void AsynCommitMessageEverOffset(KafkaConsumer<String, String> consumer, Callback callback) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
        for (ConsumerRecord<String, String> record : records) {
            long offset = record.offset();
            handleRecord(record);
            TopicPartition partition = new TopicPartition(record.topic(), record.partition());
            consumer.commitAsync(Collections.singletonMap(partition, new OffsetAndMetadata(offset + 1)), callback);
        }
    }

    /**
     * 提交消费位移的频率和拉取批次消息、处理批次消息的频率是一样
     * 将拉取到的消息存入缓存 recordBuffer，等到积累到足够多的时候，也就是示例中大于等于MIN_BATCH_SIZE时，再做相应的批量处理，之后再做异步批量提交
     *
     * @param consumer
     */
    public static void AsynCommitMessageEverPoll(KafkaConsumer<String, String> consumer, Callback callback) {
        List<ConsumerRecord<String, String>> recordBuffer = new ArrayList<>();
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
        records.forEach(recordBuffer::add);
        if (recordBuffer.size() >= MIN_BATCH_SIZE) {
            handleRecords(recordBuffer);
            consumer.commitAsync(callback);
            recordBuffer.clear();
        }
    }

    /**
     * 按分区粒度异步提交消费位移
     *
     * @param consumer
     */
    public static void AsynCommitMessageEverPartitions(KafkaConsumer<String, String> consumer, Callback callback) {
        List<ConsumerRecord<String, String>> recordBuffer = new ArrayList<>();
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
        for (TopicPartition partition : records.partitions()) {
            List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
            recordBuffer.addAll(partitionRecords);
            handleRecords(recordBuffer);
            long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
            consumer.commitAsync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)), callback);
        }

    }

    /**
     * 回调函数
     */
    static class Callback implements OffsetCommitCallback {

        @Override
        public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
            if (exception == null) {
                System.out.println(offsets);
            } else {
                System.out.println(offsets + ": " + exception);
            }
        }
    }
}
