package callofproject.dev.service.notification.config.kafka;

import callofproject.dev.service.notification.dto.NotificationUserResponseDTO;
import org.apache.kafka.clients.admin.NewTopic;
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
    private final NewTopic m_topic;
    private final KafkaTemplate<String, NotificationUserResponseDTO> m_kafkaTemplate;

    /**
     * Constructor.
     *
     * @param topic         The topic.
     * @param kafkaTemplate The kafka template.
     */
    public KafkaProducer(NewTopic topic, KafkaTemplate<String, NotificationUserResponseDTO> kafkaTemplate)
    {
        m_topic = topic;
        m_kafkaTemplate = kafkaTemplate;
    }

    /**
     * Send a message to the Kafka topic.
     *
     * @param message The message to send.
     */
    public void sendMessageToUser(NotificationUserResponseDTO message)
    {
        var msg = MessageBuilder
                .withPayload(message)
                .setHeader(TOPIC, m_topic.name())
                .build();

        m_kafkaTemplate.send(msg);
    }
}