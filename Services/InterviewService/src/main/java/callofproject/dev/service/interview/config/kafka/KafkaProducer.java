package callofproject.dev.service.interview.config.kafka;


import callofproject.dev.data.common.dto.EmailTopic;
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
    private final NewTopic m_emailTopic;
    private final KafkaTemplate<String, NotificationKafkaDTO> m_kafkaTemplate;
    private final KafkaTemplate<String, EmailTopic> m_emailKafkaTemplate;


    public KafkaProducer(@Qualifier("notificationTopic") NewTopic notificationTopic, @Qualifier("emailTopic") NewTopic emailTopic,
                         KafkaTemplate<String, NotificationKafkaDTO> kafkaTemplate, KafkaTemplate<String, EmailTopic> emailKafkaTemplate)
    {
        m_notificationTopic = notificationTopic;
        m_emailTopic = emailTopic;
        m_kafkaTemplate = kafkaTemplate;
        m_emailKafkaTemplate = emailKafkaTemplate;
    }

    public void sendNotification(NotificationKafkaDTO message)
    {
        var msg = MessageBuilder
                .withPayload(message)
                .setHeader(TOPIC, m_notificationTopic.name())
                .build();

        m_kafkaTemplate.send(msg);
    }


    /**
     * Send a message to the Kafka topic.
     *
     * @param emailTopic The message to send.
     */
    public void sendEmail(EmailTopic emailTopic)
    {
        var msg = MessageBuilder
                .withPayload(emailTopic)
                .setHeader(TOPIC, m_emailTopic.name())
                .build();

        m_emailKafkaTemplate.send(msg);
    }
}