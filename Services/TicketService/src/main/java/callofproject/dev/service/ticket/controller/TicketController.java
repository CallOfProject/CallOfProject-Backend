package callofproject.dev.service.ticket.controller;

import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.service.ticket.dto.NotificationDTO;
import callofproject.dev.service.ticket.dto.TicketDTO;
import callofproject.dev.service.ticket.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.internalServerError;
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

    @PostMapping("feedback")
    public ResponseEntity<Object> sendFeedback(@RequestBody NotificationDTO notificationDTO)
    {
        return subscribe(() -> ok(m_ticketService.sendFeedback(notificationDTO)),
                msg -> internalServerError().body(ResponseEntity.badRequest().body(msg.getMessage())));
    }

    @PostMapping("create")
    public ResponseEntity<Object> createTicket(@RequestBody TicketDTO ticketDTO)
    {
        return subscribe(() -> ok(m_ticketService.upsertTicket(ticketDTO)),
                msg -> internalServerError().body(ResponseEntity.badRequest().body(msg.getMessage())));
    }

    @GetMapping("find/all")
    public ResponseEntity<Object> findAllTickets(@RequestParam(defaultValue = "1") int page)
    {
        return subscribe(() -> ok(m_ticketService.findAllTickets(page)),
                msg -> internalServerError().body(ResponseEntity.badRequest().body(msg.getMessage())));
    }

    @GetMapping("find/all/user")
    public ResponseEntity<Object> findAllTicketsByUserId(@RequestParam("uid") UUID userId, @RequestParam(defaultValue = "1") int page)
    {
        return subscribe(() -> ok(m_ticketService.getAllTicketsByUserId(userId, page)),
                msg -> internalServerError().body(ResponseEntity.badRequest().body(msg.getMessage())));
    }

    @GetMapping("find/all/username")
    public ResponseEntity<Object> findAllTicketsByUsername(@RequestParam("username") String username, @RequestParam(defaultValue = "1") int page)
    {
        return subscribe(() -> ok(m_ticketService.getAllTicketsByUsername(username, page)),
                msg -> internalServerError().body(ResponseEntity.badRequest().body(msg.getMessage())));
    }

    @GetMapping("find/all/date")
    public ResponseEntity<Object> findAllTicketsByDate(@RequestParam("date") LocalDate date, @RequestParam(defaultValue = "1") int page)
    {
        return subscribe(() -> ok(m_ticketService.findAllByDate(date, page)),
                msg -> internalServerError().body(ResponseEntity.badRequest().body(msg.getMessage())));
    }

    @GetMapping("find/all/status")
    public ResponseEntity<Object> findAllTicketsByStatus(@RequestParam("status") EOperation status, @RequestParam(defaultValue = "1") int page)
    {
        return subscribe(() -> ok(m_ticketService.findAllByStatus(status, page)),
                msg -> internalServerError().body(ResponseEntity.badRequest().body(msg.getMessage())));
    }

    @GetMapping("find/all/date/after")
    public ResponseEntity<Object> findAllTicketsByDateAfter(@RequestParam("date") LocalDate date, @RequestParam(defaultValue = "1") int page)
    {
        return subscribe(() -> ok(m_ticketService.findAllByDateAfter(date, page)),
                msg -> internalServerError().body(ResponseEntity.badRequest().body(msg.getMessage())));
    }

    @GetMapping("find/all/date/before")
    public ResponseEntity<Object> findAllTicketsByDateBefore(@RequestParam("date") LocalDate date, @RequestParam(defaultValue = "1") int page)
    {
        return subscribe(() -> ok(m_ticketService.findAllByDateBefore(date, page)),
                msg -> internalServerError().body(ResponseEntity.badRequest().body(msg.getMessage())));
    }

    @GetMapping("find/all/date/between")
    public ResponseEntity<Object> findAllTicketsByDateBetween(@RequestParam("start") LocalDate start, @RequestParam("end") LocalDate end, @RequestParam(defaultValue = "1") int page)
    {
        return subscribe(() -> ok(m_ticketService.findAllByDateBetween(start, end, page)),
                msg -> internalServerError().body(ResponseEntity.badRequest().body(msg.getMessage())));
    }
}
