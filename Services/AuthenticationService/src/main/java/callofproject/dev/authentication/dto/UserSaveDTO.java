package callofproject.dev.authentication.dto;

import java.util.UUID;

/**
 * Data Transfer Object for saving a user.
 */
public record UserSaveDTO(String accessToken, String refreshToken, boolean success, UUID userId)
{
}
