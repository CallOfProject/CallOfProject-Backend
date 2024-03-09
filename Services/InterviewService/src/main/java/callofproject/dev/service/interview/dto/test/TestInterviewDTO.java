package callofproject.dev.service.interview.dto.test;

import callofproject.dev.service.interview.dto.ProjectDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
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
        @JsonProperty("start_time")
        @JsonFormat(pattern = "dd/MM/yyyy kk:mm:ss")
        LocalDateTime startTime,
        @JsonProperty("end_time")
        @JsonFormat(pattern = "dd/MM/yyyy kk:mm:ss")
        LocalDateTime endTime,
        @JsonProperty("total_score")
        int totalScore,
        List<QuestionDTO> questions,
        @JsonProperty("project_dto")
        ProjectDTO projectDTO
)
{
}
