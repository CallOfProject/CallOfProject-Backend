package callofproject.dev.authentication.dto;

import java.util.UUID;

public record UserSaveDTO(String accessToken, String refreshToken, boolean success, UUID userId)
{
}
