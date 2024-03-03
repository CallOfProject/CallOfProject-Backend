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
        String answer)
{
}
