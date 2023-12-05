package callofproject.dev.service.ticket.service;

import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.service.ticket.config.kafka.TicketKafkaProducer;
import callofproject.dev.service.ticket.dto.NotificationDTO;
import callofproject.dev.service.ticket.entity.Ticket;
import callofproject.dev.service.ticket.repository.ITicketRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.service.ticket.BeanName.TICKET_SERVICE;

@Service(TICKET_SERVICE)
@Lazy
@SuppressWarnings("unused")
public class TicketService
{
    private final int m_defaultPageSize = 30;
    private final ITicketRepository ticketRepository;
    private final TicketKafkaProducer m_ticketKafkaProducer;

    public TicketService(ITicketRepository ticketRepository, TicketKafkaProducer ticketKafkaProducer)
    {
        this.ticketRepository = ticketRepository;
        m_ticketKafkaProducer = ticketKafkaProducer;
    }

    public Ticket upsertTicket(Ticket ticket)
    {
        return doForDataService(() -> ticketRepository.save(ticket), "TicketService::upsertTicket");
    }

    public Optional<Ticket> getTicketById(String ticketId)
    {
        return doForDataService(() -> ticketRepository.findById(ticketId), "TicketService::getTicketById");
    }

    public void deleteTicket(String ticketId)
    {
        doForDataService(() -> ticketRepository.deleteById(ticketId), "TicketService::deleteTicket");
    }

    public void deleteAllTickets()
    {
        doForDataService(() -> ticketRepository.deleteAll(), "TicketService::deleteAllTickets");
    }

    public void deleteTicket(Ticket ticket)
    {
        ticketRepository.delete(ticket);
    }

    public boolean isTicketExist(String ticketId)
    {
        return doForDataService(() -> ticketRepository.existsById(ticketId), "TicketService::isTicketExist");
    }

    public long countTickets()
    {
        return doForDataService(() -> ticketRepository.count(), "TicketService::countTickets");
    }

    public Iterable<Ticket> getAllTickets()
    {
        return doForDataService(() -> ticketRepository.findAll(), "TicketService::getAllTickets");
    }

    public Iterable<Ticket> getAllTicketsByUserId(UUID userId, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);
        return ticketRepository.findAllByUserId(userId, pageable);
    }

    public Iterable<Ticket> getAllTicketsByUsername(String username, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);
        return doForDataService(() -> ticketRepository.findAllByUsername(username, pageable), "TicketService::getAllTicketsByUsername");
    }

    public Page<Ticket> findAllByDate(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);
        return doForDataService(() -> ticketRepository.findAllByDate(date, pageable), "TicketService::findAllByDate");
    }

    public Page<Ticket> findAllByStatus(EOperation status, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);
        return doForDataService(() -> ticketRepository.findAllByStatus(status, pageable), "TicketService::findAllByStatus");
    }

    public Page<Ticket> findAllByDateAfter(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);
        return doForDataService(() -> ticketRepository.findAllByDateAfter(date, pageable), "TicketService::findAllByDateAfter");
    }

    public Page<Ticket> findAllByDateBefore(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);
        return doForDataService(() -> ticketRepository.findAllByDateBefore(date, pageable), "TicketService::findAllByDateBefore");
    }

    public Page<Ticket> findAllByDateBetween(LocalDate start, LocalDate end, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);
        return doForDataService(() -> ticketRepository.findAllByDateBetween(start, end, pageable), "TicketService::findAllByDateBetween");
    }

    public int sendFeedback(NotificationDTO notificationDTO)
    {
        doForDataService(() -> m_ticketKafkaProducer.sendNotification(notificationDTO), "TicketService::sendFeedback");
        return Status.OK;
    }
}