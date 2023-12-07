package callofproject.dev.service.email.service;

import callofproject.dev.data.common.dto.EmailTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

import static callofproject.dev.service.email.util.Constants.TITLE_FORMAT;

/**
 * The email service.
 * CopyRight(C) 2023 by Call Of Project Teams.
 */
@Service
@Lazy
public class EmailService
{
    /**
     * The sender email.
     */
    @Value("${spring.mail.username}")
    private String senderEmail;

    /**
     * The java mail sender.
     */
    private final JavaMailSender m_javaMailSender;

    /**
     * The executor service.
     */
    private final ExecutorService m_executorService;

    /**
     * @param javaMailSender  represents the java mail sender
     * @param executorService represents the executor service
     * @implNote This constructor is used for dependency injection
     */
    public EmailService(JavaMailSender javaMailSender, ExecutorService executorService)
    {
        m_javaMailSender = javaMailSender;
        m_executorService = executorService;
    }

    /**
     * @param emailTopic represents the email topic
     * @implNote This method is used for sending the email
     */
    private void send(EmailTopic emailTopic)
    {
        var message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(emailTopic.getToEmail());
        message.setSubject(String.format(TITLE_FORMAT, emailTopic.getTitle()));
        message.setText(emailTopic.getMessage());
        m_javaMailSender.send(message);
    }

    /**
     * @param emailTopic represents the email topic
     * @implNote This method is used for sending the email asynchronously
     *
     */
    public void sendEmail(EmailTopic emailTopic)
    {
        m_executorService.execute(() -> send(emailTopic));
    }
}
