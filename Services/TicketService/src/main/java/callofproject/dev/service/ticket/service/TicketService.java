package callofproject.dev.service.ticket.service;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.service.ticket.config.kafka.TicketKafkaProducer;
import callofproject.dev.service.ticket.dto.NotificationDTO;
import callofproject.dev.service.ticket.dto.TicketDTO;
import callofproject.dev.service.ticket.entity.Ticket;
import callofproject.dev.service.ticket.mapper.ITicketMapper;
import callofproject.dev.service.ticket.repository.ITicketRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final ITicketMapper m_ticketMapper;

    public TicketService(ITicketRepository ticketRepository, TicketKafkaProducer ticketKafkaProducer, ITicketMapper ticketMapper)
    {
        this.ticketRepository = ticketRepository;
        m_ticketKafkaProducer = ticketKafkaProducer;
        m_ticketMapper = ticketMapper;
    }

    public ResponseMessage<Object> upsertTicket(TicketDTO ticket)
    {
        return doForDataService(() -> new ResponseMessage<>("ticket upserted successfully!", Status.CREATED,
                ticketRepository.save(m_ticketMapper.toTicket(ticket))), "TicketService::upsertTicket");
    }


    public ResponseMessage<Object> getTicketById(String ticketId)
    {
        var ticket = doForDataService(() -> ticketRepository.findById(ticketId), "TicketService::getTicketById");

        return new ResponseMessage<>("ticket retrieved successfully!", Status.OK, ticket.orElse(null));
    }

    public ResponseMessage<Object> deleteTicket(String ticketId)
    {
        doForDataService(() -> ticketRepository.deleteById(ticketId), "TicketService::deleteTicket");
        return new ResponseMessage<>("ticket deleted successfully!", Status.OK, true);
    }

    public ResponseMessage<Object> deleteAllTickets()
    {
        doForDataService(() -> ticketRepository.deleteAll(), "TicketService::deleteAllTickets");
        return new ResponseMessage<>("All tickets deleted successfully!", Status.OK, true);
    }

    public ResponseMessage<Object> deleteTicket(Ticket ticket)
    {
        doForDataService(() -> ticketRepository.delete(ticket), "TicketService::deleteTicket");
        return new ResponseMessage<>("ticket deleted successfully!", Status.OK, true);
    }

    public boolean isTicketExist(String ticketId)
    {
        return doForDataService(() -> ticketRepository.existsById(ticketId), "TicketService::isTicketExist");
    }

    public long countTickets()
    {
        return doForDataService(() -> ticketRepository.count(), "TicketService::countTickets");
    }

    public MultipleResponseMessage<Object> getAllTickets()
    {
        var tickets = doForDataService(() -> ticketRepository.findAll(), "TicketService::getAllTickets");
        return new MultipleResponseMessage<>(tickets.size(), "tickets retrieved successfully!", tickets);
    }

    public MultipleResponseMessagePageable<Object> getAllTicketsByUserId(UUID userId, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);
        var tickets = doForDataService(() -> ticketRepository.findAllByUserId(userId, pageable), "TicketService::getAllTicketsByUserId");
        return new MultipleResponseMessagePageable<>(tickets.getTotalPages(), page, tickets.stream().toList().size(), "tickets retrieved successfully!", tickets.stream().toList());
    }

    public MultipleResponseMessagePageable<Object> getAllTicketsByUsername(String username, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);
        var tickets = doForDataService(() -> ticketRepository.findAllByUsername(username, pageable), "TicketService::getAllTicketsByUsername");
        return new MultipleResponseMessagePageable<>(tickets.getTotalPages(), page, tickets.stream().toList().size(), "tickets retrieved successfully!", tickets.stream().toList());
    }

    public MultipleResponseMessagePageable<Object> findAllByDate(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);
        var tickets = doForDataService(() -> ticketRepository.findAllByDate(date, pageable), "TicketService::findAllByDate");
        return new MultipleResponseMessagePageable<>(tickets.getTotalPages(), page, tickets.stream().toList().size(), "tickets retrieved successfully!", tickets.stream().toList());
    }

    public MultipleResponseMessagePageable<Object> findAllByStatus(EOperation status, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);
        var tickets = doForDataService(() -> ticketRepository.findAllByStatus(status, pageable), "TicketService::findAllByStatus");
        return new MultipleResponseMessagePageable<>(tickets.getTotalPages(), page, tickets.stream().toList().size(), "tickets retrieved successfully!", tickets.stream().toList());
    }

    public MultipleResponseMessagePageable<Object> findAllByDateAfter(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);
        var tickets = doForDataService(() -> ticketRepository.findAllByDateAfter(date, pageable), "TicketService::findAllByDateAfter");
        return new MultipleResponseMessagePageable<>(tickets.getTotalPages(), page, tickets.stream().toList().size(), "tickets retrieved successfully!", tickets.stream().toList());
    }

    public MultipleResponseMessagePageable<Object> findAllByDateBefore(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);
        var tickets = doForDataService(() -> ticketRepository.findAllByDateBefore(date, pageable), "TicketService::findAllByDateBefore");
        return new MultipleResponseMessagePageable<>(tickets.getTotalPages(), page, tickets.stream().toList().size(), "tickets retrieved successfully!", tickets.stream().toList());
    }

    public MultipleResponseMessagePageable<Object> findAllByDateBetween(LocalDate start, LocalDate end, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);
        var tickets = doForDataService(() -> ticketRepository.findAllByDateBetween(start, end, pageable), "TicketService::findAllByDateBetween");
        return new MultipleResponseMessagePageable<>(tickets.getTotalPages(), page, tickets.stream().toList().size(), "tickets retrieved successfully!", tickets.stream().toList());
    }

    public MultipleResponseMessagePageable<Object> findAllTickets(int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);
        var tickets = doForDataService(() -> ticketRepository.findAllTickets(pageable), "TicketService::findAllTickets");
        var totalPage = tickets.getTotalPages();

        return new MultipleResponseMessagePageable<>(totalPage, page, tickets.stream().toList().size(), "tickets retrieved successfully!", tickets.stream().toList());
    }

    public ResponseMessage<Object> sendFeedback(NotificationDTO notificationDTO)
    {
        doForDataService(() -> m_ticketKafkaProducer.sendNotification(notificationDTO), "TicketService::sendFeedback");
        return new ResponseMessage<>("feedback sent successfully!", Status.OK, true);
    }
}