    package callofproject.dev.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailTopic
{
    @JsonProperty("email_type")
    private EmailType emailType;
    private String toEmail;

    public EmailTopic()
    {
    }

    public EmailTopic(EmailType emailType, String toEmail)
    {
        this.emailType = emailType;
        this.toEmail = toEmail;
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
