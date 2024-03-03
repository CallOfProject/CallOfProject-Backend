package callofproject.dev.service.interview.dto.test;

public record QuestionDTO(
        long id,
        String question,
        String option1,
        String option2,
        String option3,
        String option4,
        int point
)
{
}
