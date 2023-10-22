package consumer.demo;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.Test;
import util.ConsumerUtil;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 自定义消费者拦截器
 */
public class ConsumerInterceptorTest {
    public static final String topic = "topic-demo";
    public static final AtomicBoolean isRunning = new AtomicBoolean(true);
    public static final int MIN_BATCH_SIZE = 200;

    @Test
    public void test_consumer_interceptor() {
        Properties properties = ConsumerUtil.initConfig();
        properties.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, MyConsumerInterceptor.class.getName());
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(topic));
        AsyncCommitDemo.Callback callback = new AsyncCommitDemo.Callback();
        try {
            while (isRunning.get()) {
                //AsynCommitMessageEverOffset(consumer,callback);
                //AsynCommitMessageEverPoll(consumer,callback);
                AsyncCommitDemo.AsynCommitMessageEverPartitions(consumer, callback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.commitSync();
            consumer.unsubscribe();
            consumer.close();
        }
    }
}
