package callofproject.dev.service.ticket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * TicketCreateDTO
 */
public record TicketCreateDTO(
        @JsonProperty("user_id")
        UUID userId,
        String username,
        String title,
        String userEmail,
        String description)
{
}
