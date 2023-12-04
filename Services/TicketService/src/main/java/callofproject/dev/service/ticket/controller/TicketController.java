package callofproject.dev.service.ticket.controller;

import callofproject.dev.service.ticket.service.TicketService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/ticket")
public class TicketController
{
    private final TicketService m_ticketService;

    public TicketController(TicketService ticketService)
    {
        m_ticketService = ticketService;
    }

}
