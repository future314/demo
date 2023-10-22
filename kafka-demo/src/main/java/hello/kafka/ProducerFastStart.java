package hello.kafka;

import cn.hutool.core.date.DateUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import util.ProducerUtil;

import java.util.Properties;

/**
 * @author LR
 */
public class ProducerFastStart {
    public static final String topic = "topic-demo";
    public static final int count = 1;

    public static KafkaProducer<String, String> getProducer() {
        Properties properties = ProducerUtil.initConfig();
        return new KafkaProducer<>(properties);
    }

    public static void main(String[] args) {
        KafkaProducer<String, String> kafkaProducer = getProducer();
        for (int i = 0; i < count; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, DateUtil.now() + "");
            try {
                kafkaProducer.send(record);
                System.out.println("已发送：" + record);
                //Thread.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        kafkaProducer.close();
    }
}
