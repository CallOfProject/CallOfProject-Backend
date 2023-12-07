package callofproject.dev.service.ticket.repository;

import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.service.ticket.entity.Ticket;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

import static callofproject.dev.service.ticket.BeanName.TICKET_REPOSITORY;

@Repository(TICKET_REPOSITORY)
@Lazy
public interface ITicketRepository extends MongoRepository<Ticket, String>
{
    @Query("{}")
    Page<Ticket> findAllTickets(Pageable pageable);
    Page<Ticket> findAllByUserId(UUID userId, Pageable pageable);

    Page<Ticket> findAllByUsername(String username, Pageable pageable);

    Page<Ticket> findAllByDate(LocalDate date, Pageable pageable);

    Page<Ticket> findAllByStatus(EOperation status, Pageable pageable);

    //@Query("{ 'date' : { $gt : ?0 } }")
    Page<Ticket> findAllByDateAfter(LocalDate date, Pageable pageable);

    // @Query("{ 'date' : { $lt : ?0 } }")
    Page<Ticket> findAllByDateBefore(LocalDate date, Pageable pageable);

    //@Query("{ 'date' : { $gte : ?0, $lte : ?1 } }")
    Page<Ticket> findAllByDateBetween(LocalDate start, LocalDate end, Pageable pageable);
}
