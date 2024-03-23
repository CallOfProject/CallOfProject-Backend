package callofproject.dev.service.ticket.service;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.data.common.enums.EmailType;
import callofproject.dev.data.common.enums.NotificationType;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.service.ticket.EmailTemplateUtil;
import callofproject.dev.service.ticket.config.kafka.TicketKafkaProducer;
import callofproject.dev.service.ticket.dto.NotificationKafkaDTO;
import callofproject.dev.service.ticket.dto.TicketCreateDTO;
import callofproject.dev.service.ticket.dto.TicketDTO;
import callofproject.dev.service.ticket.dto.TicketGiveResponseDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.service.ticket.BeanName.TICKET_SERVICE;
import static java.lang.String.format;

/**
 * @author Nuri Can ÖZTÜRK
 * Represents the ticket service.
 * CopyRight(C) 2023 by Call Of Project Teams.
 */
@Service(TICKET_SERVICE)
@Lazy
@SuppressWarnings("unused")
public class TicketService implements ITicketService
{
    private final TicketCallbackService m_callbackService;
    private final TicketKafkaProducer m_ticketKafkaProducer;

    public TicketService(TicketCallbackService callbackService, TicketKafkaProducer ticketKafkaProducer)
    {
        m_callbackService = callbackService;
        m_ticketKafkaProducer = ticketKafkaProducer;
    }

    /**
     * Upsert ticket.
     *
     * @param dto represents the ticket create dto
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> upsertTicket(TicketCreateDTO dto)
    {
        return doForDataService(() -> m_callbackService.upsertTicketCallback(dto), "TicketService::upsertTicket");
    }

    /**
     * Admin or Root roles can respond the ticket.
     *
     * @param dto represents the ticket dto
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> responseFeedback(TicketGiveResponseDTO dto)
    {
        var result = doForDataService(() -> m_callbackService.responseFeedbackCallback(dto), "TicketService::responseFeedback");

        if (result.getStatusCode() == Status.OK)
        {
            sendNotification((TicketDTO) result.getObject(), dto);
            sendEmail((TicketDTO) result.getObject(), dto);
        }

        return result;
    }

    /**
     * Find all tickets.
     *
     * @return the multiple response message
     */
    @Override
    public MultipleResponseMessage<Object> findAllTickets()
    {
        return doForDataService(m_callbackService::findAllTicketsCallback, "TicketService::findAllTicketsPageable");
    }

    /**
     * Find all tickets pageable.
     *
     * @param page represents the page
     * @return the multiple response message pageable
     */
    @Override
    public MultipleResponseMessagePageable<Object> findAllTicketsPageable(int page)
    {
        return doForDataService(() -> m_callbackService.findAllTicketsPageableCallback(page), "TicketService::findAllTicketsPageable");
    }

    /**
     * Find all open tickets.
     *
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> findOpenTicketCount()
    {
        return doForDataService(m_callbackService::findOpenTicketCount, "TicketService::findOpenTicketCount");
    }

    /**
     * Find all closed tickets.
     *
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> findClosedTicketCount()
    {
        return doForDataService(m_callbackService::findClosedTicketCount, "TicketService::findClosedTicketCount");
    }


    private void sendNotification(TicketDTO ticket, TicketGiveResponseDTO dto)
    {
        var msg = format("Ticket with title: %s is answered by %s. Please check your email!", ticket.title(), dto.adminUsername());
        var notificationMessage = new NotificationKafkaDTO.Builder()
                .setFromUserId(ticket.adminId())
                .setToUserId(ticket.userId())
                .setMessage(msg)
                .setNotificationType(NotificationType.INFORMATION)
                .setNotificationLink("none")
                .build();

        m_ticketKafkaProducer.sendNotification(notificationMessage);
    }

    private void sendEmail(TicketDTO ticket, TicketGiveResponseDTO dto)
    {
        var createdDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(ticket.createdDate());
        var responseDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(ticket.answeredDate());
        var template = EmailTemplateUtil.getEmailTemplate("ticket_response.html");
        var msg = format(template, ticket.title(), createdDate, responseDate, ticket.status(), dto.answer());
        var emailTopic = new EmailTopic(EmailType.REMAINDER, ticket.userEmail(), "Ticket Answered!", msg, null);
        m_ticketKafkaProducer.sendEmail(emailTopic);
    }
}