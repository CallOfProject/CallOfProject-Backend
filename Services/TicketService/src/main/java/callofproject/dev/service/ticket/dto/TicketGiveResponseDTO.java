package callofproject.dev.service.ticket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;


/**
 * TicketGiveResponseDTO
 */
public record TicketGiveResponseDTO(
        String id, // Ticket id
        @JsonProperty("admin_id")
        UUID adminId,
        String answer,
        @JsonProperty("admin_username")
        String adminUsername)
{
}
