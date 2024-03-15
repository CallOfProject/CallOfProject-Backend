package callofproject.dev.service.ticket.controller;

import callofproject.dev.library.exception.util.ExceptionUtil;
import callofproject.dev.service.ticket.dto.TicketCreateDTO;
import callofproject.dev.service.ticket.dto.TicketGiveResponseDTO;
import callofproject.dev.service.ticket.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/ticket")
public class TicketController
{
    private final TicketService m_ticketService;

    public TicketController(TicketService ticketService)
    {
        m_ticketService = ticketService;
    }


    @PostMapping("create")
    public ResponseEntity<Object> createTicket(@RequestBody TicketCreateDTO ticketCreateDTO)
    {
        return subscribe(() -> ResponseEntity.ok(m_ticketService.upsertTicket(ticketCreateDTO)),
                msg -> ResponseEntity.badRequest().body(msg.getMessage()));
    }

    @GetMapping("/find/open-count")
    public ResponseEntity<Object> getOpenTicketCount()
    {
        return subscribe(() -> ok(m_ticketService.findOpenTicketCount()),
                msg -> ResponseEntity.badRequest().body(msg.getMessage()));
    }

    @GetMapping("/find/all")
    public ResponseEntity<Object> getAllTickets(@RequestParam("page") int page)
    {
        return subscribe(() -> ok(m_ticketService.findAllTicketsPageable(page)),
                msg -> ResponseEntity.badRequest().body(msg.getMessage()));
    }

    @PostMapping("response")
    public ResponseEntity<Object> responseFeedback(@RequestBody TicketGiveResponseDTO dto)
    {
        return subscribe(() -> ResponseEntity.ok(m_ticketService.responseFeedback(dto)),
                msg -> ResponseEntity.badRequest().body(msg.getMessage()));
    }
}
