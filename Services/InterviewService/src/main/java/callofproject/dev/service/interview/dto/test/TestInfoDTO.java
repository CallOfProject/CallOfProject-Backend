package callofproject.dev.service.interview.dto.test;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record TestInfoDTO(
        @JsonProperty("_id")
        UUID testInterviewId,
        @JsonProperty("question_count")
        int questionCount,
        @JsonProperty("duration_time")
        long totalTimeMinutes
)
{
}
