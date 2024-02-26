package callofproject.dev.service.interview.dto.test;

import java.util.List;
import java.util.UUID;

public record CreateTestDTO(
        String title,
        int questionCount,
        String description,
        long totalTimeMinutes,
        int totalScore,
        UUID projectId,
        List<CreateQuestionDTO> questions)
{
}
