package callofproject.dev.service.email.service;

import callofproject.dev.data.common.dto.EmailTopic;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
    @Value("${spring.mail.username}")
    private String senderEmail;
    private final JavaMailSender m_javaMailSender;
    private final ExecutorService m_executorService;

    /**
     * Constructor.
     *
     * @param javaMailSender  represents the java mail sender
     * @param executorService represents the executor service
     */
    public EmailService(JavaMailSender javaMailSender, ExecutorService executorService)
    {
        m_javaMailSender = javaMailSender;
        m_executorService = executorService;
    }

    /**
     * Email sending.
     *
     * @param emailTopic represents the email topic
     */
    private void send(EmailTopic emailTopic) throws MessagingException
    {
        MimeMessage message = m_javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(senderEmail);
        helper.setTo(emailTopic.getToEmail());
        helper.setSubject(String.format(TITLE_FORMAT, emailTopic.getTitle()));
        helper.setText(emailTopic.getMessage(), true);
        m_javaMailSender.send(message);
    }

    /**
     * Send email asynchronously.
     *
     * @param emailTopic represents the email topic
     */
    public void sendEmail(EmailTopic emailTopic)
    {
        m_executorService.execute(() -> {
            try
            {
                send(emailTopic);
            } catch (MessagingException e)
            {
                e.printStackTrace();
            }
        });
    }
}
