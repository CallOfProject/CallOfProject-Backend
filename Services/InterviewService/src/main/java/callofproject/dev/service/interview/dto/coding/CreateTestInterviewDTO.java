package callofproject.dev.service.interview.dto.coding;

import java.util.List;
import java.util.UUID;

public record CreateTestInterviewDTO(
        UUID projectId,
        int questionCount,
        String title,
        String description,
        long totalTimeMinutes,
        int totalScore,
        List<TestInterviewQuestionDTO> questions)
{
}
