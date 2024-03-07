package callofproject.dev.service.scheduler.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

/**
 * KafkaTopic
 */
@Component
public class KafkaTopic
{

    @Value("${spring.kafka.email-topic-name}")
    private String m_emailTopicName;
    @Value("${spring.kafka.notification-topic-name}")
    private String m_notificationTopicName;

    /**
     * Constructs a new KafkaTopic.
     */
    public KafkaTopic()
    {
    }


    /**
     * Create a new topic.
     *
     * @return The topic.
     */
    @Bean("emailTopic")
    public NewTopic provideEmailTopic()
    {
        return TopicBuilder.name(m_emailTopicName).build();
    }

    @Bean("notificationTopic")
    public NewTopic provideNotificationTopic()
    {
        return TopicBuilder.name(m_notificationTopicName).build();
    }
}
