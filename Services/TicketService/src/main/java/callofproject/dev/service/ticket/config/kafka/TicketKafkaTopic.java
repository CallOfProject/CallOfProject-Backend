package callofproject.dev.service.ticket.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

@Component
public class TicketKafkaTopic
{
    @Value("${spring.kafka.notification-topic-name}")
    private String m_topicName;

    @Bean
    public NewTopic provideNotificationTopic()
    {
        return TopicBuilder.name(m_topicName).build();
    }
}
