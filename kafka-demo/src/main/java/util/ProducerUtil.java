package util;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author LR
 */
public class ProducerUtil {
    public static final String brokerList = "192.168.137.10:9092";
    public static final String topic = "topic-demo";

    public static Properties initConfig() {
        return initConfig(StringSerializer.class);
    }
    public static Properties initConfig(Class<?> valueSerializer) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer.getName());
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        return properties;
    }
}
