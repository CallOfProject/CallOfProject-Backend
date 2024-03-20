package callofproject.dev.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ParticipantRateDTO(
        @JsonProperty("rate_owner_id")
        UUID rateOwnerId,
        @JsonProperty("rate_user_id")
        UUID rateUserId,
        @JsonProperty("project_id")
        UUID projectId,
        double rate)
{
}
