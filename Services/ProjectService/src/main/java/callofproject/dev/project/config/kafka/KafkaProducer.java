package callofproject.dev.project.config.kafka;

import callofproject.dev.project.dto.ProjectParticipantRequestDTO;
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
    private final KafkaTemplate<String, ProjectParticipantRequestDTO> m_projectParticipantKafkaTemplate;

    public KafkaProducer(NewTopic topic, KafkaTemplate<String, ProjectParticipantRequestDTO> kafkaTemplate)
    {
        m_topic = topic;
        m_projectParticipantKafkaTemplate = kafkaTemplate;
    }

    /**
     * Send a message to the Kafka topic.
     *
     * @param message The message to send.
     */
    public void sendProjectParticipantNotification(ProjectParticipantRequestDTO message)
    {
        var msg = MessageBuilder
                .withPayload(message)
                .setHeader(TOPIC, m_topic.name())
                .build();

        m_projectParticipantKafkaTemplate.send(msg);
    }
}