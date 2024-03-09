package callofproject.dev.service.interview.dto;

import callofproject.dev.data.interview.entity.enums.InterviewResult;
import callofproject.dev.data.interview.entity.enums.InterviewStatus;
import callofproject.dev.service.interview.dto.coding.CodingInterviewDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record UserCodingInterviewDTO(
        UUID id,
        List<UserDTO> users,
        @JsonProperty("coding_interview")
        CodingInterviewDTO codingInterview,
        @JsonProperty("interview_result")
        InterviewResult interviewResult,
        @JsonProperty("interview_status")
        InterviewStatus interviewStatus,
        @JsonProperty("answer_file_link")
        String answerFileUrl,
        @JsonProperty("project")
        ProjectDTO projectDTO)
{
}
