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
    @Value("${spring.kafka.projectInfo-topic-name}")
    private String m_projectInfoTopicName;

    @Value("${spring.kafka.participant-topic-name}")
    private String m_participantTopicName;

    /**
     * Constructs a new KafkaTopicProvider.
     */
    public KafkaTopicProvider()
    {
    }

    /**
     * Create a new Kafka topic.
     *
     * @return The newly created Kafka notification topic.
     */
    @Bean("notificationTopic")
    public NewTopic provideNotificationTopic()
    {
        return TopicBuilder.name(m_notificationTopicName).build();
    }

    /**
     * Create a new Kafka topic.
     *
     * @return The newly created projectInfo Kafka topic.
     */
    @Bean("projectInfoTopic")
    public NewTopic provideProjectInfoTopic()
    {
        return TopicBuilder.name(m_projectInfoTopicName).build();
    }

    /**
     * Create a new Kafka topic.
     *
     * @return The newly created projectParticipant Kafka topic.
     */
    @Bean("projectParticipantTopic")
    public NewTopic provideProjectParticipantTopic()
    {
        return TopicBuilder.name(m_participantTopicName).build();
    }
}
