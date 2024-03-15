package callofproject.dev.service.ticket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

public record TicketUserViewDTO(
        @JsonProperty("user_id")
        UUID userId,
        String username,
        String title,
        String description,
        String answer,

        @JsonProperty("user_email")
        String userEmail,
        @JsonProperty("feedback_deadline")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate feedbackDeadline)

{
}
