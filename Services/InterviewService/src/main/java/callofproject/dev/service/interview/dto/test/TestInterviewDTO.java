package callofproject.dev.service.interview.dto.test;

import callofproject.dev.service.interview.dto.ProjectDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TestInterviewDTO(
        @JsonProperty("interview_id")
        String id,
        String title,
        @JsonProperty("question_count")
        int questionCount,
        String description,
        @JsonProperty("duration_minutes")
        long totalTimeMinutes,
        @JsonProperty("total_score")
        int totalScore,
        List<QuestionDTO> questions,
        @JsonProperty("project_dto")
        ProjectDTO projectDTO
)
{
}
