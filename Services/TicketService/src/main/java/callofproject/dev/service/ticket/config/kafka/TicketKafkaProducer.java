package callofproject.dev.service.ticket.config.kafka;

import callofproject.dev.service.ticket.dto.NotificationDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static callofproject.dev.service.ticket.BeanName.TICKET_KAFKA_PRODUCER;

@Service(TICKET_KAFKA_PRODUCER)
public class TicketKafkaProducer
{
    private final NewTopic m_topic;
    private final KafkaTemplate<String, NotificationDTO> m_kafkaTemplate;

    public TicketKafkaProducer(NewTopic topic, KafkaTemplate<String, NotificationDTO> kafkaTemplate)
    {
        m_topic = topic;
        m_kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(NotificationDTO notificationDTO)
    {
        var message = MessageBuilder.withPayload(notificationDTO)
                .setHeader(KafkaHeaders.TOPIC, m_topic.name())
                .build();

        m_kafkaTemplate.send(message);
    }
}