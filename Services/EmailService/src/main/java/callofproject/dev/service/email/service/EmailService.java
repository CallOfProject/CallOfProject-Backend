package callofproject.dev.service.email.service;

import callofproject.dev.service.email.Util;
import callofproject.dev.service.email.config.kafka.EmailKafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ExecutorService;

@Service
@Lazy
public class EmailService
{
    @Value("${spring.mail.username}")
    private String senderEmail;
    private final JavaMailSender m_javaMailSender;
    private final EmailKafkaProducer m_kafkaProducer;
    private final ExecutorService m_executorService;
    private final Random m_random;

    public EmailService(JavaMailSender javaMailSender, EmailKafkaProducer kafkaProducer, ExecutorService executorService, Random random)
    {
        m_javaMailSender = javaMailSender;
        m_kafkaProducer = kafkaProducer;
        m_executorService = executorService;
        m_random = random;
    }

    public void sendAuthenticationMail(String toEmail)
    {
        var verificationCode = Util.generateVerificationCode(m_random);
        var subject = "Email Verification";
        var text = "Your verification code is: " + verificationCode;
        m_executorService.execute(() -> sendEmail(toEmail, subject, text));
    }

    public void sendEmail(String toEmail, String subject, String text)
    {
        var message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(toEmail);
        message.setSubject(String.format(Util.TITLE_FORMAT, subject));
        message.setText(text);
        m_javaMailSender.send(message);
        m_executorService.shutdown();
    }
}
