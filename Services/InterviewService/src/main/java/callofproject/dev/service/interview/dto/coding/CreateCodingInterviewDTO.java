package callofproject.dev.service.interview.dto.coding;

public record CreateCodingInterviewDTO(
        String title,
        String description,
        long durationMinutes,
        String question,
        String answerFileName,
        int point,
        String projectId)
{
}
