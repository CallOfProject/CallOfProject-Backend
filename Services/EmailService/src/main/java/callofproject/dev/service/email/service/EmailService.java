package callofproject.dev.service.email.service;

import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.service.email.config.kafka.EmailKafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

import static callofproject.dev.service.email.Constants.TITLE_FORMAT;

@Service
@Lazy
public class EmailService
{
    @Value("${spring.mail.username}")
    private String senderEmail;
    private final JavaMailSender m_javaMailSender;
    private final EmailKafkaProducer m_kafkaProducer;
    private final ExecutorService m_executorService;

    public EmailService(JavaMailSender javaMailSender, EmailKafkaProducer kafkaProducer, ExecutorService executorService)
    {
        m_javaMailSender = javaMailSender;
        m_kafkaProducer = kafkaProducer;
        m_executorService = executorService;
    }

    private void send(EmailTopic emailTopic)
    {
        var message = new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(emailTopic.getToEmail());
        message.setSubject(String.format(TITLE_FORMAT, emailTopic.getTitle()));
        message.setText(emailTopic.getMessage());
        m_javaMailSender.send(message);
    }

    public void sendEmail(EmailTopic emailTopic)
    {
        m_executorService.execute(() -> send(emailTopic));
    }
}
