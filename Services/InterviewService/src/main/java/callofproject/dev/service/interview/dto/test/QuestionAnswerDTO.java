package callofproject.dev.service.interview.dto.test;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record QuestionAnswerDTO(
        @JsonProperty("user_id")
        UUID userId,
        @JsonProperty("interview_id")
        UUID interviewId,
        @JsonProperty("question_id")
        long questionId,
        String question,
        String answer,
        String option1,
        String option2,
        String option3,
        String option4,
        @JsonProperty("correct_answer")
        String correctAnswer)
{
}
