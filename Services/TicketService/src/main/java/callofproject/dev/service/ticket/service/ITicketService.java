package callofproject.dev.service.ticket.service;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.service.ticket.dto.TicketCreateDTO;
import callofproject.dev.service.ticket.dto.TicketGiveResponseDTO;

/**
 * @author Nuri Can ÖZTÜRK
 * Represents the ticket service.
 * CopyRight(C) 2023 by Call Of Project Teams.
 */
public interface ITicketService
{
    /**
     * Upsert ticket.
     *
     * @param dto represents the ticket create dto
     * @return the response message
     */
    ResponseMessage<Object> upsertTicket(TicketCreateDTO dto);

    /**
     * Response feedback.
     *
     * @param dto represents the ticket give response dto
     * @return the response message
     */
    ResponseMessage<Object> responseFeedback(TicketGiveResponseDTO dto);

    /**
     * Find open ticket count.
     *
     * @return the response message
     */
    ResponseMessage<Object> findOpenTicketCount();

    /**
     * Find closed ticket count.
     *
     * @return the response message
     */
    ResponseMessage<Object> findClosedTicketCount();

    /**
     * Find all tickets.
     *
     * @return the multiple response message
     */
    MultipleResponseMessage<Object> findAllTickets();

    /**
     * Find all tickets pageable.
     *
     * @param page represents the page
     * @return the multiple response message pageable
     */
    MultipleResponseMessagePageable<Object> findAllTicketsPageable(int page);
}
