package callofproject.dev.service.interview.config.kafka;


import callofproject.dev.service.interview.dto.NotificationKafkaDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
public class KafkaProducer
{
    private final NewTopic m_notificationTopic;
    private final KafkaTemplate<String, NotificationKafkaDTO> m_kafkaTemplate;


    public KafkaProducer(@Qualifier("notificationTopic") NewTopic notificationTopic,
                         KafkaTemplate<String, NotificationKafkaDTO> kafkaTemplate)
    {
        m_notificationTopic = notificationTopic;
        m_kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(NotificationKafkaDTO message)
    {
        var msg = MessageBuilder
                .withPayload(message)
                .setHeader(TOPIC, m_notificationTopic.name())
                .build();

        m_kafkaTemplate.send(msg);
    }
}
