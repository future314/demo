package consumer.demo;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import util.ConsumerUtil;

import java.time.Duration;
import java.util.*;

/**
 * @author LR
 * <p>
 * 再均衡监听器
 */
public class ConsumerRebalanceListenerDemo {
    public static final String topic = "topic-demo";

    public void startConsumer() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(ConsumerUtil.initConfig());
        Map<TopicPartition, OffsetAndMetadata> tpOffset = new HashMap<>();
        consumer.subscribe(Arrays.asList(topic), new ConsumerRebalanceListener() {
            @Override
            // 再均衡开始之前和消费者停止读取消息之后被调用
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                consumer.commitSync(tpOffset);
                tpOffset.clear();
            }

            @Override
            // 重新分配分区之后和消费者开始读取消费之前被调用
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
              /*  for (TopicPartition tp : partitions) {
                    consumer.seek(tp, tpOffset.get(tp).offset());
                }*/
            }
        });

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record);
                tpOffset.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1));
            }
            consumer.commitAsync(tpOffset, (offsets, exception) -> offsets.keySet().forEach(tpOffset::remove));
        }
    }
}
