package callofproject.dev.service.filterandsearch.dto;

import callofproject.dev.data.project.entity.enums.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record ProjectFilterDTO(
        @JsonProperty("profession_level")
        EProjectProfessionLevel professionLevel,
        @JsonProperty("project_level")
        EProjectLevel projectLevel,
        EDegree degree,
        @JsonProperty("feedback_time_range")
        EFeedbackTimeRange feedbackTimeRange,
        @JsonProperty("interview_type")
        EInterviewType interviewType,
        @JsonProperty("project_status")
        EProjectStatus projectStatus,
        @JsonProperty("start_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate startDate,
        @JsonProperty("expected_completion_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate expectedCompletionDate,
        @JsonProperty("application_deadline")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate applicationDeadline,
        String keyword)
{
}
