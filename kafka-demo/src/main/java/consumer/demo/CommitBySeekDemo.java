package consumer.demo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;
import util.ConsumerUtil;

import java.time.Duration;
import java.util.*;

/**
 * 指定位移消费
 *
 * @author LR
 */
public class CommitBySeekDemo {
    public static final String topic = "topic-demo";

    public static void main(String[] args) throws InterruptedException {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(ConsumerUtil.initConfig());
        consumer.subscribe(Arrays.asList(topic));
        //seekByOffset(consumer);
        seekByTimestamp(consumer);

    }

    /**
     * 直接指定位移消费
     */
    public static void seekByOffset(KafkaConsumer<String, String> consumer) {
        Set<TopicPartition> assignment = new HashSet<>();
        while (true) {
            while (assignment.size() == 0) {
                consumer.poll(Duration.ofMillis(100));
                assignment = consumer.assignment();
            }
            // 获取分区开头位移
            Map<TopicPartition, Long> offsets = consumer.beginningOffsets(assignment);
            // 获取分区末尾位移
            // Map<TopicPartition, Long> offsets = consumer.endOffsets(assignment);
            for (TopicPartition tp : assignment) {
                consumer.seek(tp, offsets.get(tp));
            }
            // 指定位移为分区头
            //consumer.seekToBeginning(assignment);
            // 指定位移为分区末尾
            //consumer.seekToEnd(assignment);

            for (ConsumerRecord<String, String> record : consumer.poll(Duration.ofMillis(1000))) {
                System.out.println(record);
            }
            // Thread.sleep(1000 * 5);
            System.out.println("--------------");
        }
    }

    /**
     * 指定时间消费
     */
    public static void seekByTimestamp(KafkaConsumer<String, String> consumer) {
        Map<TopicPartition, Long> timestampMap = new HashMap<>();
        long timestamp = DateUtil.parse("2023-07-12 9:31", DatePattern.NORM_DATETIME_MINUTE_PATTERN).getTime();
        Set<TopicPartition> assignment = new HashSet<>();
        while (assignment.size() == 0) {
            consumer.poll(Duration.ofMillis(100));
            assignment = consumer.assignment();
        }
        Set<TopicPartition> topicPartitionSet = consumer.assignment();
        for (TopicPartition tp : topicPartitionSet) {
            timestampMap.put(tp, timestamp);
        }
        // 返回时间戳大于等于待查询时间的第一条消息对应的位置和时间戳
        Map<TopicPartition, OffsetAndTimestamp> offsets = consumer.offsetsForTimes(timestampMap);
        while (true) {
            for (TopicPartition tp : topicPartitionSet) {
                consumer.seek(tp, offsets.get(tp).offset());
            }
            for (ConsumerRecord<String, String> record : consumer.poll(Duration.ofMillis(1000))) {
                System.out.println(record);
            }
            System.out.println("--------------");
        }
    }

}
