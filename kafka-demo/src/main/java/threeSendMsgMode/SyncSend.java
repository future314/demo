package threeSendMsgMode;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import util.ProducerUtil;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * @author LR
 */
public class SyncSend {
    public static final int count = 10;

    public static void main(String[] args) {
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(ProducerUtil.initConfig());
        for (int i = 0; i < count; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(ProducerUtil.topic, System.currentTimeMillis() + "");
            try {
                Future<RecordMetadata> future = kafkaProducer.send(record);
                RecordMetadata metadata = future.get();
                System.out.println("已发送：" + record + ";" + metadata.topic());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        kafkaProducer.close();
    }
}

