package callofproject.dev.service.ticket.config.kafka;

import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.service.ticket.dto.NotificationKafkaDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static callofproject.dev.service.ticket.BeanName.TICKET_KAFKA_PRODUCER;
import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

/**
 * @author Nuri Can ÖZTÜRK
 * The ticket kafka producer.
 * CopyRight(C) 2023 by Call Of Project Teams.
 */
@Service(TICKET_KAFKA_PRODUCER)
public class TicketKafkaProducer
{
    private final NewTopic m_topic;
    private final NewTopic m_emailTopic;
    private final KafkaTemplate<String, NotificationKafkaDTO> m_notificationKafkaTemplate;
    private final KafkaTemplate<String, EmailTopic> m_emailKafkaTemplate;


    /**
     * Constructor.
     *
     * @param topic                     represents the topic
     * @param emailTopic                represents the email topic
     * @param notificationKafkaTemplate represents the notification kafka template
     * @param emailKafkaTemplate        represents the email kafka template
     */
    public TicketKafkaProducer(@Qualifier("notificationTopic") NewTopic topic,
                               @Qualifier("emailTopic") NewTopic emailTopic,
                               KafkaTemplate<String, NotificationKafkaDTO> notificationKafkaTemplate,
                               KafkaTemplate<String, EmailTopic> emailKafkaTemplate)
    {
        m_emailKafkaTemplate = emailKafkaTemplate;
        m_emailTopic = emailTopic;
        m_topic = topic;
        m_notificationKafkaTemplate = notificationKafkaTemplate;
    }

    /**
     * Send notification.
     *
     * @param notificationDTO represents the notification dto
     */
    public void sendNotification(NotificationKafkaDTO notificationDTO)
    {
        var message = MessageBuilder
                .withPayload(notificationDTO)
                .setHeader(KafkaHeaders.TOPIC, m_topic.name())
                .build();

        m_notificationKafkaTemplate.send(message);
    }

    /**
     * Send email.
     *
     * @param emailTopic represents the email topic
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