package consumer.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import util.ConsumerUtil;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * 通过分区消费
 * @author LR
 */
public class ConsumerByPartition {
    public static final String topic = "topic-demo";

    public static KafkaConsumer<String, String> getConsumer() {
        Properties properties = ConsumerUtil.initConfig();
        return new KafkaConsumer<>(properties);
    }

    public static void main(String[] args) {
        KafkaConsumer<String, String> kafkaConsumer = getConsumer();
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));
            // 获取消息集中所有分区
            for(TopicPartition tp:records.partitions()){
                // records.records(tp):分区维度  records.records(topic)主题维度
                for (ConsumerRecord<String, String> record : records.records(tp)) {
                    System.out.println(record);
                }
            }
        }
    }
}
