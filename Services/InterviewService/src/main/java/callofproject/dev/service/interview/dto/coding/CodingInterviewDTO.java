package callofproject.dev.service.interview.dto.coding;

import callofproject.dev.data.interview.entity.enums.InterviewStatus;
import callofproject.dev.service.interview.dto.ProjectDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record CodingInterviewDTO(
        @JsonProperty("interview_id")
        UUID codingInterviewId,
        String title,
        String description,
        @JsonProperty("duration_minutes")
        long durationMinutes,
        @JsonProperty("interview_status")
        InterviewStatus interviewStatus,
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
