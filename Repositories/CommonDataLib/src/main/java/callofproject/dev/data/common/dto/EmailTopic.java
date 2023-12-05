package callofproject.dev.data.common.dto;

import callofproject.dev.data.common.enums.EmailType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailTopic
{
    @JsonProperty("email_type")
    private EmailType emailType;
    private String toEmail;
    private String title;
    private String message;
    private Object object;

    public EmailTopic()
    {
    }

    public EmailTopic(EmailType emailType, String toEmail, String title, String message, Object object)
    {
        this.emailType = emailType;
        this.toEmail = toEmail;
        this.title = title;
        this.message = message;
        this.object = object;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Object getObject()
    {
        return object;
    }

    public void setObject(Object object)
    {
        this.object = object;
    }

    public String getToEmail()
    {
        return toEmail;
    }

    public void setToEmail(String toEmail)
    {
        this.toEmail = toEmail;
    }

    public EmailType getEmailType()
    {
        return emailType;
    }

    public void setEmailType(EmailType emailType)
    {
        this.emailType = emailType;
    }
}
