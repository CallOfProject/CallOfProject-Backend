package callofproject.dev.service.scheduler.dto;

import java.util.UUID;

public record UserMatchDTO(
        String username,
        UUID userId,
        String firstName,
        String middleName,
        String lastName,
        String link,
        String email,
        String image)
{
}
