package callofproject.dev.service.interview.dto.test;

import java.util.List;
import java.util.UUID;

public record AssignMultipleInterviewDTO(
        UUID interviewId,
        UUID projectId,
        List<UUID> userId

)
{
}
