package callofproject.dev.service.interview.dto.coding;

public record TestInterviewQuestionDTO(
        String question,
        String option1,
        String option2,
        String option3,
        String option4,
        String answer,
        int point)
{
}
