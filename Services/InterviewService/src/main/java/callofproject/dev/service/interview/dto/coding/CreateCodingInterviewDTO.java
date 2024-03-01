package callofproject.dev.service.interview.dto.coding;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateCodingInterviewDTO(
        String title,
        String question,
        String description,
        @JsonProperty("duration_minutes")
        long durationMinutes,
        int point,
        @JsonProperty("project_id")
        UUID projectId,
        @JsonProperty("start_time")
        @JsonFormat(pattern = "dd/MM/yyyy kk:mm:ss")
        LocalDateTime startTime,
        @JsonProperty("end_time")
        @JsonFormat(pattern = "dd/MM/yyyy kk:mm:ss")
        LocalDateTime endTime,
        @JsonProperty("user_ids")
        List<String> userIds)
{
}
