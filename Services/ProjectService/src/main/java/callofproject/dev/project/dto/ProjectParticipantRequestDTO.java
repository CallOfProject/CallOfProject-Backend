package callofproject.dev.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * ProjectParticipantRequestDTO
 */
public record ProjectParticipantRequestDTO(
        @JsonProperty("user_id")
        UUID userId,
        @JsonProperty("project_id")
        UUID projectId,
        @JsonProperty("request_id")
        UUID requestId
)
{
}
