package callofproject.dev.service.notification.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

/**
 * KafkaTopic
 */
@Component
public class KafkaTopic
{
    @Value("${spring.kafka.producer.topic-name}")
    private String m_topicName;

    /**
     * Default constructor.
     */
    public KafkaTopic()
    {
    }

    /**
     * Create a new topic.
     *
     * @return The topic.
     */
    @Bean
    public NewTopic provideTopic()
    {
        return TopicBuilder.name(m_topicName).build();
    }
}
