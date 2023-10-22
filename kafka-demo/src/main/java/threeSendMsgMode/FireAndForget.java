package threeSendMsgMode;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import util.ProducerUtil;

/**
 * @author LR
 */
public class FireAndForget {

    public static final int count = 10;

    public static void main(String[] args) {
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(ProducerUtil.initConfig());
        for (int i = 0; i < count; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(ProducerUtil.topic, System.currentTimeMillis() + "");
            try {
                kafkaProducer.send(record);
                System.out.println("已发送：" + record);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        kafkaProducer.close();
    }
}
