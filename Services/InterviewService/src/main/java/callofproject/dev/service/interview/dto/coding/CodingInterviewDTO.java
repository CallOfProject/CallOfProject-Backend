package callofproject.dev.service.interview.dto.coding;

import callofproject.dev.service.interview.dto.ProjectDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record CodingInterviewDTO(
        String title,
        String description,
        @JsonProperty("duration_minutes")
        long durationMinutes,
        String question,
        int point,
        @JsonProperty("start_time")
        @JsonFormat(pattern = "dd/MM/yyyy kk:mm:ss")
        LocalDateTime startTime,
        @JsonProperty("end_time")
        @JsonFormat(pattern = "dd/MM/yyyy kk:mm:ss")
        LocalDateTime endTime,
        ProjectDTO projectDTO
)
{
}
