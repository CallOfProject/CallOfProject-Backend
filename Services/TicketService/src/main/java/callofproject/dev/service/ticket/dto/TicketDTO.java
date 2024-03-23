package callofproject.dev.service.ticket.dto;

import callofproject.dev.service.ticket.entity.TicketStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

/**
 * The ticket data transfer object.
 * CopyRight(C) 2023 by Call Of Project Teams.
 */
public record TicketDTO(
        String id,
        @JsonProperty("user_id")
        UUID userId,
        @JsonProperty("admin_id")
        UUID adminId,
        @JsonProperty("admin_username")
        String adminUsername,
        String username,
        @JsonProperty("user_email")
        String userEmail,
        String title,
        @JsonProperty("feedback_deadline")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate feedbackDeadline,
        @JsonProperty("answered_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate answeredDate,
        @JsonProperty("created_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate createdDate,

        String description,
        TicketStatus status)
{
}
