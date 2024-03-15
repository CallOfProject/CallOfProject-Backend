package callofproject.dev.service.ticket.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.library.exception.util.CopDataUtil;
import callofproject.dev.service.ticket.dto.TicketCreateDTO;
import callofproject.dev.service.ticket.dto.TicketDTO;
import callofproject.dev.service.ticket.dto.TicketGiveResponseDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.service.ticket.BeanName.TICKET_SERVICE;
import static java.lang.String.format;

@Service(TICKET_SERVICE)
@Lazy
@SuppressWarnings("unused")
public class TicketService
{
    private final TicketCallbackService m_callbackService;

    public TicketService(TicketCallbackService callbackService)
    {
        m_callbackService = callbackService;
    }

    public ResponseMessage<Object> upsertTicket(TicketCreateDTO dto)
    {
        return doForDataService(() -> m_callbackService.upsertTicketCallback(dto), "TicketService::upsertTicket");
    }

    public ResponseMessage<Object> responseFeedback(TicketGiveResponseDTO dto)
    {
        var result = doForDataService(() -> m_callbackService.responseFeedbackCallback(dto), "TicketService::responseFeedback");

        if (result.getStatusCode() == Status.OK)
        {
            m_callbackService.sendNotification((TicketDTO) result.getObject());
            m_callbackService.sendEmail((TicketDTO) result.getObject());
        }

        return result;
    }

    public MultipleResponseMessagePageable<Object> findAllTicketsPageable(int page)
    {
        return doForDataService(() -> m_callbackService.findAllTicketsPageableCallback(page), "TicketService::findAllTicketsPageable");
    }

    public ResponseMessage<Object> findOpenTicketCount()
    {
        return doForDataService(m_callbackService::findOpenTicketCount, "TicketService::findOpenTicketCount");
    }
}