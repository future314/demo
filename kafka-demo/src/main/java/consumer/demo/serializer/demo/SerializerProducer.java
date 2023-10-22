package consumer.demo.serializer.demo;

import cn.hutool.core.date.DateUtil;
import consumer.demo.serializer.Company;
import consumer.demo.serializer.ProtostuffSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import util.ProducerUtil;

import java.util.Properties;

/**
 * @author LR
 */
public class SerializerProducer {
    public static final String topic = "topic-demo";
    public static final int count = 1;

    public static KafkaProducer<String, Company> getProducer() {
        Properties properties = ProducerUtil.initConfig(ProtostuffSerializer.class);
        return new KafkaProducer<>(properties);
    }

    public static void main(String[] args) {
        KafkaProducer<String, Company> kafkaProducer = getProducer();
        for (int i = 0; i < count; i++) {
            ProducerRecord<String, Company> record = new ProducerRecord<>(topic, new Company("aa","bb"));
            try {
                kafkaProducer.send(record);
                System.out.println("已发送：" + record);
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        kafkaProducer.close();
    }
}
