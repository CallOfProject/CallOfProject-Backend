package callofproject.dev.project.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

/**
 * This class provides configuration for creating Kafka topics.
 */
@Component
public class KafkaTopicProvider
{
    @Value("${spring.kafka.notification-topic-name}")
    private String m_notificationTopicName;

    /**
     * Constructs a new KafkaTopicProvider.
     */
    public KafkaTopicProvider()
    {
    }

    /**
     * Create a new Kafka topic.
     *
     * @return The newly created Kafka topic.
     */
    @Bean
    public NewTopic provideNotificationTopic()
    {
        return TopicBuilder.name(m_notificationTopicName).build();
    }
}
