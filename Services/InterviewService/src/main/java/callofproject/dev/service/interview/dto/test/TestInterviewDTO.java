package callofproject.dev.service.interview.dto.test;

import java.util.List;

public record TestInterviewDTO(
        String title,
        int questionCount,
        String description,
        long totalTimeMinutes,
        int totalScore,
        List<QuestionDTO> questions
)
{
}
