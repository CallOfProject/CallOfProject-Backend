package callofproject.dev.service.email.config;

import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.service.email.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Represents the email kafka consumer.
 * CopyRight(C) 2023 by Call Of Project Teams.
 */
@Service
public class EmailKafkaConsumer
{
    private final EmailService m_emailService;


    /**
     * Constructor.
     *
     * @param emailService represents the email service
     */
    public EmailKafkaConsumer(EmailService emailService)
    {
        m_emailService = emailService;
    }

    /**
     * Listen to the email topic.
     *
     * @param emailTopic represents the email topic
     */
    @KafkaListener(topics = "${spring.kafka.email-topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenEmailTopic(EmailTopic emailTopic)
    {
        m_emailService.sendEmail(emailTopic);
    }
}