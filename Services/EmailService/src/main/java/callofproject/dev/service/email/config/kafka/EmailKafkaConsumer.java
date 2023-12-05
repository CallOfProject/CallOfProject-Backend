package callofproject.dev.service.email.config.kafka;

import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.service.email.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmailKafkaConsumer
{
    private final EmailService m_emailService;

    public EmailKafkaConsumer(EmailService emailService)
    {
        m_emailService = emailService;
    }

    @KafkaListener(topics = "${spring.kafka.email-topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenEmailTopic(EmailTopic emailTopic)
    {
        m_emailService.sendEmail(emailTopic);
    }
}
