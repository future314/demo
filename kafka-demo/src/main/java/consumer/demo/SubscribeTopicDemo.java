package consumer.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import util.ConsumerUtil;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
 * 定义主题demo
 * 通过 subscribe（）方法订阅主题具有消费者自动再均衡的功能，
 * 在多个消费者的情况下可以根据分区分配策略来自动分配各个消费者与分区的关系。
 * 当消费组内的消费者增加或减少时，分区分配关系会自动调整，以实现消费负载均衡及故障自动转移。
 * 而通过assign（）方法订阅分区时，不具备消费者自动均衡的功能.
 *
 * @author LR
 */
public class SubscribeTopicDemo {
    public static final String topic = "topic-demo";
    public static final String topicPattern = "topic-.*";
    public static final AtomicBoolean isRunning = new AtomicBoolean(true);

    public static void main(String[] args) {
        Properties properties = ConsumerUtil.initConfig();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        subscribeByCollection(consumer);
        //subscribeByPattern(consumer);
        //subscribeByAssign(consumer);
        try {
            while (isRunning.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record.topic());
                    System.out.println(record.partition());
                    System.out.println(record.offset());
                    System.out.println(record.key());
                    System.out.println(record.value());
                    System.out.println("------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            unsubscribe(consumer);
            consumer.close();
        }
    }

    /**
     * 使用集合的方式来订阅主题
     *
     * @param consumer
     */
    public static void subscribeByCollection(KafkaConsumer<String, String> consumer) {
        consumer.subscribe(Arrays.asList(topic));
    }

    /**
     * 使用正则表达式的方式订阅主题，如果有人订阅之后又创建了新的主题，
     * 并且主题的名字与正则表达式相匹配，那么这个消费者就可以消费到新添加的主题中的消息
     *
     * @param consumer
     */
    public static void subscribeByPattern(KafkaConsumer<String, String> consumer) {
        Pattern pattern = Pattern.compile(topicPattern);
        consumer.subscribe(pattern);
    }

    /**
     * 直接订阅主题的特定分区
     *
     * @param consumer
     */
    public static void subscribeByAssign(KafkaConsumer<String, String> consumer) {
        List<TopicPartition> partitions = new ArrayList<>();
        // 查询主题的元数据信息
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
        if (partitions != null) {
            // 订阅主题的所有分区
            for (PartitionInfo partitionInfo : partitionInfos) {
                partitions.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
            }
        }
        consumer.assign(partitions);
    }

    /**
     * 三种取消订阅主题的方式
     *
     * @param consumer
     */
    public static void unsubscribe(KafkaConsumer<String, String> consumer) {
        consumer.unsubscribe();
        //consumer.subscribe(new ArrayList<String>());
        //consumer.assign(new ArrayList<TopicPartition>());
    }
}
