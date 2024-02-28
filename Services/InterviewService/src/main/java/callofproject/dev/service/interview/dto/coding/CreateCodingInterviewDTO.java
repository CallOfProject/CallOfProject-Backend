package callofproject.dev.service.interview.dto.coding;

import java.util.UUID;

public record CreateCodingInterviewDTO(
        String title,
        String description,
        long durationMinutes,
        String question,
        int point,
        UUID projectId)
{
}
