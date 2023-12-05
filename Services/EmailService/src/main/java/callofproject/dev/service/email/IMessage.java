package callofproject.dev.service.email;

import callofproject.dev.service.email.dto.EmailSendDTO;

public interface IMessage
{
    String sendEmail(EmailSendDTO emailSendDTO);
}
