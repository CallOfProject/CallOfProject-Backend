package callofproject.dev.service.scheduler.config.kafka;

import callofproject.dev.data.common.dto.EmailTopic;
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
    private final NewTopic m_emailTopic;
    private final KafkaTemplate<String, EmailTopic> m_emailKafkaTemplate;


    /**
     * Constructor for the KafkaProducer class.
     * It is used to inject dependencies into the service.
     *
     * @param emailTopic         The NewTopic object to be injected.
     * @param emailKafkaTemplate The KafkaTemplate object to be injected.
     */
    public KafkaProducer(NewTopic emailTopic, KafkaTemplate<String, EmailTopic> emailKafkaTemplate)
    {
        m_emailTopic = emailTopic;
        m_emailKafkaTemplate = emailKafkaTemplate;
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