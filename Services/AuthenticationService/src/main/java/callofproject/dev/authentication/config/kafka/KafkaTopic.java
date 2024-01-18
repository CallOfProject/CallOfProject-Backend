package callofproject.dev.authentication.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

/**
 * KafkaTopic
 */
@Component
public class KafkaTopic
{
    @Value("${spring.kafka.topic-name}")
    private String m_authenticationTopic;

    @Value("${spring.kafka.email-topic-name}")
    private String m_emailTopicName;

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
    @Bean
    @Primary
    public NewTopic provideTopic()
    {
        return TopicBuilder.name(m_authenticationTopic).build();
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
}
