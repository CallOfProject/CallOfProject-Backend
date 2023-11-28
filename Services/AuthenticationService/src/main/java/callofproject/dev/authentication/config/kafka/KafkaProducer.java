package callofproject.dev.authentication.config.kafka;

import callofproject.dev.authentication.dto.UserKafkaDTO;
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
    private final KafkaTemplate<String, UserKafkaDTO> m_kafkaTemplate;

    public KafkaProducer(NewTopic topic, KafkaTemplate<String, UserKafkaDTO> kafkaTemplate)
    {
        m_topic = topic;
        m_kafkaTemplate = kafkaTemplate;
    }

    /**
     * Send a message to the Kafka topic.
     *
     * @param message The message to send.
     */
    public void sendMessage(UserKafkaDTO message)
    {
        var msg = MessageBuilder
                .withPayload(message)
                .setHeader(TOPIC, m_topic.name())
                .build();

        m_kafkaTemplate.send(msg);
    }
}