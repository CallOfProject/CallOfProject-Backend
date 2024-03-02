package callofproject.dev.service.interview.dto.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateTestDTO(
        String title,
        @JsonProperty("question_count")
        int questionCount,
        String description,
        @JsonProperty("duration_minutes")
        long totalTimeMinutes,
        @JsonProperty("total_score")
        int totalScore,
        @JsonProperty("project_id")
        UUID projectId,
        @JsonProperty("start_time")
        @JsonFormat(pattern = "dd/MM/yyyy kk:mm:ss")
        LocalDateTime startTime,
        @JsonProperty("end_time")
        @JsonFormat(pattern = "dd/MM/yyyy kk:mm:ss")
        LocalDateTime endTime,
        @JsonProperty("question_list")
        List<CreateQuestionDTO> questionList,
        @JsonProperty("user_ids")
        List<String> userIds)
{
}
