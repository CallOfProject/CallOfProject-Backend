package callofproject.dev.service.ticket.repository;

import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.service.ticket.entity.Ticket;
import callofproject.dev.service.ticket.entity.TicketStatus;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static callofproject.dev.service.ticket.BeanName.TICKET_REPOSITORY;

@Repository(TICKET_REPOSITORY)
@Lazy
public interface ITicketRepository extends MongoRepository<Ticket, String>
{
    @Query("{}")
    Page<Ticket> findAllTickets(Pageable pageable);

    Page<Ticket> findAllByStatus(TicketStatus status, Pageable pageable);

    List<Ticket> findTicketsByStatus(TicketStatus ticketStatus);

    List<Ticket> findAllByFeedbackDeadline(LocalDate feedbackDeadline);
}
