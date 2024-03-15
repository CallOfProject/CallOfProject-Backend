package callofproject.dev.service.ticket.mapper;

import callofproject.dev.service.ticket.dto.TicketCreateDTO;
import callofproject.dev.service.ticket.dto.TicketDTO;
import callofproject.dev.service.ticket.dto.TicketUserViewDTO;
import callofproject.dev.service.ticket.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(implementationName = "TicketMapperImpl", componentModel = "spring")
public interface ITicketMapper
{
    Ticket toTicket(TicketDTO ticketDTOEEx);

    TicketDTO toTicketDTO(Ticket ticket);

    Ticket toTicket(TicketCreateDTO ticketDTO);

    TicketUserViewDTO toTicketUserViewDTO(Ticket ticket);
}
