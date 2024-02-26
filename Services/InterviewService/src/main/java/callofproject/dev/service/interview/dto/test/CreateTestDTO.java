package callofproject.dev.service.interview.dto.test;

public record CreateTestDTO(
        String title,
        String description,
        long durationMinutes,
        String question,
        String answerFileName,
        int point,
        String projectId
)
{
}
