package callofproject.dev.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * ParticipantRequestDTO
 */
public record   ParticipantRequestDTO(
        @JsonProperty("request_id")
        UUID requestId,
        @JsonProperty("notification_id")
        String notificationId,
        @JsonProperty("is_accepted")
        boolean isAccepted)
{
}
