package callofproject.dev.service.interview.dto.test;

import java.util.UUID;

public record TestInterviewFinishDTO(
        UUID projectId,
        UUID interviewId,
        UUID userId
)
{
}
