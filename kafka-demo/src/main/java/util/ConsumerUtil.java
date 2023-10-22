package util;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

/**
 * @author LR
 */
public class ConsumerUtil {
    public static final String brokerList = "192.168.137.10:9092";
    public static final String groupID = "group.demo";

    public static Properties initConfig() {
        return initConfig(StringDeserializer.class);
    }

    public static Properties initConfig(Class<?> valueDeserializer) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer.getName());
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupID);
        return properties;
    }
}
