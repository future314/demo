package consumer.demo.serializer.demo;

import consumer.demo.serializer.Company;
import consumer.demo.serializer.ProtostuffDeserializer;
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
public class DeserializerConsumer {
    public static final String topic = "topic-demo";

    public static KafkaConsumer<String, Company> getConsumer() {
        Properties properties = ConsumerUtil.initConfig(ProtostuffDeserializer.class);
        return new KafkaConsumer<>(properties);
    }

    public static void main(String[] args) {
        KafkaConsumer<String, Company> kafkaConsumer = getConsumer();
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        while (true) {
            ConsumerRecords<String, Company> records = kafkaConsumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, Company> record : records) {
                System.out.println(record.value());
            }
        }
    }
}
