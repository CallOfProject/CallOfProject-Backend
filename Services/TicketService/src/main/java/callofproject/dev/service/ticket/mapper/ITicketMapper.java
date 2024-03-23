package callofproject.dev.service.ticket.mapper;

import callofproject.dev.service.ticket.dto.TicketCreateDTO;
import callofproject.dev.service.ticket.dto.TicketDTO;
import callofproject.dev.service.ticket.dto.TicketUserViewDTO;
import callofproject.dev.service.ticket.entity.Ticket;
import org.mapstruct.Mapper;

/**
 * @author Nuri Can ÖZTÜRK
 * Represents the ticket mapper.
 * CopyRight(C) 2023 by Call Of Project Teams.
 */
@Mapper(implementationName = "TicketMapperImpl", componentModel = "spring")
public interface ITicketMapper
{
    /**
     * Converts the ticket to ticket DTO.
     *
     * @param ticket represents the ticket
     * @return The ticket DTO.
     */
    TicketDTO toTicketDTO(Ticket ticket);

    /**
     * Converts the ticket create DTO to ticket.
     *
     * @param ticketDTO represents the ticket create DTO
     * @return The ticket.
     */
    Ticket toTicket(TicketCreateDTO ticketDTO);

    /**
     * Converts the ticket to ticket user view DTO.
     *
     * @param ticket represents the ticket
     * @return The ticket user view DTO.
     */
    TicketUserViewDTO toTicketUserViewDTO(Ticket ticket);
}
