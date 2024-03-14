package callofproject.dev.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object for updating a user profile.
 */
public record UserProfileUpdateDTO(
        @JsonProperty("user_rate")
        double userRate,
        @JsonProperty("user_feedback_rate")
        double userFeedbackRate,
        @JsonProperty("user_id")
        String userId,
        @JsonProperty("about_me")
        String aboutMe)
{

        @Override
        public String toString()
        {
                return "UserProfileUpdateDTO{" +
                        "userRate=" + userRate +
                        ", userFeedbackRate=" + userFeedbackRate +
                        ", userId=" + userId +
                        ", aboutMe='" + aboutMe + '\'' +
                        '}';
        }
}
