package callofproject.dev.project.config.kafka;

import callofproject.dev.project.dto.ProjectParticipantNotificationDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

/**
 * This class represents a Kafka producer service responsible for sending messages to a Kafka topic.
 */
@Service
public class KafkaProducer
{
    private final NewTopic m_topic;
    private final KafkaTemplate<String, ProjectParticipantNotificationDTO> m_projectParticipantKafkaTemplate;

    /**
     * Constructs a new KafkaProducer with the provided dependencies.
     *
     * @param topic         The NewTopic instance representing the Kafka topic to send messages to.
     * @param kafkaTemplate The KafkaTemplate instance for sending messages to the Kafka topic.
     */
    public KafkaProducer(NewTopic topic, KafkaTemplate<String, ProjectParticipantNotificationDTO> kafkaTemplate)
    {
        m_topic = topic;
        m_projectParticipantKafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends a ProjectParticipantNotificationDTO message to the Kafka topic.
     *
     * @param message The message to send.
     */
    public void sendProjectParticipantNotification(ProjectParticipantNotificationDTO message)
    {
        var msg = MessageBuilder
                .withPayload(message)
                .setHeader(TOPIC, m_topic.name())
                .build();
        m_projectParticipantKafkaTemplate.send(msg);
    }
}