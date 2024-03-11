package callofproject.dev.service.interview.dto;

import java.util.UUID;

public record InterviewResultDTO(
        UUID ownerId,
        UUID userId,
        String projectName,
        String message,
        String email
)
{
}
