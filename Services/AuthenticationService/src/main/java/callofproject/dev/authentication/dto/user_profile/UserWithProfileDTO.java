package callofproject.dev.authentication.dto.user_profile;

import callofproject.dev.authentication.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Data Transfer Object for a user with profile.
 */
public record UserWithProfileDTO(
        @JsonProperty("user_id")
        UUID id,
        UserDTO user,
        UserProfileDTO profile)
{
}
