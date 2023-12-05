package callofproject.dev.service.email.config.kafka;

import callofproject.dev.service.email.dto.EmailTopic;
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
        System.out.println(emailTopic.getToEmail());
        switch (emailTopic.getEmailType())
        {
            case EMAIL_VERIFICATION -> m_emailService.sendAuthenticationMail(emailTopic.getToEmail());
            case PASSWORD_RESET -> throw new UnsupportedOperationException("TODO");
            case PROJECT_RECOMMENDATION -> throw new UnsupportedOperationException("TODO");
            default -> throw new UnsupportedOperationException("Invalid email type");
        }
    }
}
