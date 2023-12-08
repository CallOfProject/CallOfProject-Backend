package callofproject.dev.authentication.dto.user_profile;

import callofproject.dev.authentication.dto.UserDTO;

public record UserWithProfileDTO(
        UserDTO user,
        UserProfileDTO profile)
{
}
