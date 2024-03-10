package callofproject.dev.service.interview.dto;

import callofproject.dev.data.interview.entity.enums.InterviewResult;
import callofproject.dev.data.interview.entity.enums.InterviewStatus;
import callofproject.dev.service.interview.dto.test.QuestionAnswerDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record UserTestInterviewDTO(
        UUID id,
        UserDTO user,
        @JsonProperty("test_interview")
        TestInterviewDTO testInterview,
        @JsonProperty("interview_result")
        InterviewResult interviewResult,
        @JsonProperty("interview_status")
        InterviewStatus interviewStatus,
        @JsonProperty("user_answers")
        List<QuestionAnswerDTO> userAnswers,
        @JsonProperty("project")
        ProjectDTO projectDTO
)
{
}
