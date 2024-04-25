package callofproject.dev.service.filterandsearch.dto;

import java.util.UUID;

public record UserDTO(
        UUID userId,
        String username,
        String firstName,
        String middleName,
        String lastName,
        String image
)
{
}
