package hello.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import util.ConsumerUtil;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * @author LR
 */
public class ConsumerFastStart {
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
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.value());
            }
        }
    }
}
