package callofproject.dev.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ProjectRateDTO(
        @JsonProperty("project_id")
        UUID projectId,
        @JsonProperty("user_id")
        UUID userId,
        @JsonProperty("rate")
        int rate)
{
}
