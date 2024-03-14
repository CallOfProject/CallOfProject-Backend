package callofproject.dev.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Data Transfer Object for updating a user profile.
 */
public record UserProfileUpdateDTO(
        @JsonProperty("updated_user_id")
        UUID userId,
        @JsonProperty("about_me")
        String aboutMe)
{

}
