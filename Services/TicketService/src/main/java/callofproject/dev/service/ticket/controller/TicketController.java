package callofproject.dev.service.ticket.controller;

import callofproject.dev.service.ticket.dto.NotificationDTO;
import callofproject.dev.service.ticket.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/admin/ticket")
public class TicketController
{
    private final TicketService m_ticketService;

    public TicketController(TicketService ticketService)
    {
        m_ticketService = ticketService;
    }

    @PostMapping("feedback")
    public ResponseEntity<Object> sendFeedback(@RequestBody NotificationDTO notificationDTO)
    {
        return subscribe(() -> ok(m_ticketService.sendFeedback(notificationDTO)),
                msg -> internalServerError().body(ResponseEntity.badRequest().body(msg.getMessage())));
    }
}
