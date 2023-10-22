package kafkaAdmin.demo;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.config.ConfigResource;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author LR
 */
public class KafkaAdminDemo {
    public static final String brokerList = "192.168.137.10:9092";
    public static final String topic = "create_topic";

    public static AdminClient initAdminClient() {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        properties.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        return AdminClient.create(properties);
    }

    public static void createTopic() throws ExecutionException, InterruptedException {
        AdminClient client = initAdminClient();
        NewTopic newTopic = new NewTopic(topic, 4, (short) 1);
        CreateTopicsResult result = client.createTopics(Collections.singleton(newTopic));
        result.all().get();
        client.close();
    }

    public static void describeTopicConfig() throws ExecutionException, InterruptedException {
        AdminClient client = initAdminClient();
        ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, topic);

        DescribeConfigsResult result =client.describeConfigs(Collections.singleton(resource));
        Config config = result.all().get().get(resource);
        System.out.println(config);
        client.close();
    }
}
