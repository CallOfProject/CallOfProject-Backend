package callofproject.dev.authentication.dto.user_profile;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserRateDTO(
        @JsonProperty("user_rate")
        double userRate,
        @JsonProperty("user_feedback_rate")
        double userFeedbackRate)
{
}
