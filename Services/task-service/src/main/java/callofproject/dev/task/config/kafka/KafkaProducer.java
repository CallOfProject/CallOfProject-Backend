package callofproject.dev.task.config.kafka;

import callofproject.dev.task.dto.NotificationKafkaDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

/**
 * This class is a Spring Service class that provides Kafka producer functionality.
 */
@Service
public class KafkaProducer
{
    private final NewTopic m_notificationTopic;
    private final KafkaTemplate<String, NotificationKafkaDTO> m_kafkaTemplate;


    /**
     * Default constructor.
     *
     * @param notificationTopic The Kafka notification topic.
     * @param kafkaTemplate     The Kafka template.
     */
    public KafkaProducer(@Qualifier("notificationTopic") NewTopic notificationTopic,
                         KafkaTemplate<String, NotificationKafkaDTO> kafkaTemplate)
    {
        m_notificationTopic = notificationTopic;
        m_kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends a notification message to the Kafka topic.
     *
     * @param message The notification message to send.
     */
    public void sendNotification(NotificationKafkaDTO message)
    {
        var msg = MessageBuilder
                .withPayload(message)
                .setHeader(TOPIC, m_notificationTopic.name())
                .build();

        m_kafkaTemplate.send(msg);
    }
}
