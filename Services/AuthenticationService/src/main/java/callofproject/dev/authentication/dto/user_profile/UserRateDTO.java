package callofproject.dev.authentication.dto.user_profile;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object for a user rate.
 */
public record UserRateDTO(
        @JsonProperty("user_rate")
        double userRate,
        @JsonProperty("user_feedback_rate")
        double userFeedbackRate)
{
}
