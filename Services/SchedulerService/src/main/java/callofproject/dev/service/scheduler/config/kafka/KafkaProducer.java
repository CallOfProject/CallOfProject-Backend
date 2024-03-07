package callofproject.dev.service.scheduler.config.kafka;

import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.service.scheduler.dto.NotificationKafkaDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

/**
 * Kafka producer.
 */
@Service
public class KafkaProducer
{
    private final NewTopic m_emailTopic;
    private final NewTopic m_notificationTopic;
    private final KafkaTemplate<String, EmailTopic> m_emailKafkaTemplate;
    private final KafkaTemplate<String, NotificationKafkaDTO> m_notificationKafkaTemplate;


    /**
     * Constructor for the KafkaProducer class.
     * It is used to inject dependencies into the service.
     *
     * @param emailTopic         The NewTopic object to be injected.
     * @param emailKafkaTemplate The KafkaTemplate object to be injected.
     */
    public KafkaProducer(@Qualifier("emailTopic") NewTopic emailTopic,
                         @Qualifier("notificationTopic") NewTopic notificationTopic,
                         KafkaTemplate<String, EmailTopic> emailKafkaTemplate,
                         KafkaTemplate<String, NotificationKafkaDTO> notificationKafkaTemplate)
    {
        m_emailTopic = emailTopic;
        m_notificationTopic = notificationTopic;
        m_emailKafkaTemplate = emailKafkaTemplate;
        m_notificationKafkaTemplate = notificationKafkaTemplate;
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


    public void sendNotification(NotificationKafkaDTO message)
    {
        var msg = MessageBuilder
                .withPayload(message)
                .setHeader(TOPIC, m_notificationTopic.name())
                .build();

        m_notificationKafkaTemplate.send(msg);
    }
}