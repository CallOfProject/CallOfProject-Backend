package callofproject.dev.project.dto;

import callofproject.dev.data.project.entity.enums.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ProjectUpdateDTO(
        @JsonProperty("user_id")
        @NotNull(message = "user id cannot be empty")
        UUID userId,
        @JsonProperty("project_id")
        @NotNull(message = "project id cannot be empty")
        UUID projectId,
        @JsonProperty("project_image")
        String projectImage,
        @JsonProperty("project_name")
        @NotBlank(message = "project name cannot be empty")
        @NotEmpty(message = "project name cannot be empty")
        String projectName,
        @JsonProperty("project_summary")
        @NotBlank(message = "project summary cannot be empty")
        @NotEmpty(message = "project summary cannot be empty")
        String projectSummary,
        @JsonProperty("project_description")
        @NotBlank(message = "project description cannot be empty")
        @NotEmpty(message = "project description cannot be empty")
        String projectDescription,
        @JsonProperty("project_aim")
        @NotBlank(message = "project aim cannot be empty")
        @NotEmpty(message = "project aim cannot be empty")
        String projectAim,
        @JsonProperty("project_deadline")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        @NotNull(message = "project deadline cannot be empty")
        LocalDate applicationDeadline,
        @JsonProperty("expected_completion_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        @NotNull(message = "expected completion date cannot be empty")
        LocalDate expectedCompletionDate,

        @JsonProperty("max_participant_count")
        @NotNull
        @Min(value = 2, message = "max participant count cannot be less than 2")
        @Max(value = 20, message = "max participant count cannot be more than 100")
        int maxParticipantCount,
        @JsonProperty("technical_requirements")
        @NotBlank(message = "technical requirements cannot be empty")
        @NotEmpty(message = "technical requirements cannot be empty")
        String technicalRequirements,
        @JsonProperty("special_requirements")
        @NotBlank(message = "special requirements cannot be empty")
        @NotEmpty(message = "special requirements cannot be empty")
        String specialRequirements,
        @JsonProperty("project_access_type")
        @NotNull(message = "project access type cannot be empty")
        EProjectAccessType projectAccessType,
        @JsonProperty("project_profession_level")
        @NotNull(message = "project profession level cannot be empty")
        EProjectProfessionLevel professionLevel,
        @JsonProperty("project_sector")
        @NotNull(message = "project sector cannot be empty")
        ESector sector,
        @JsonProperty("project_degree")
        @NotNull(message = "project degree cannot be empty")
        EDegree degree,
        @JsonProperty("project_level")
        @NotNull(message = "project level cannot be empty")
        EProjectLevel projectLevel,
        @JsonProperty("project_interview_type")
        @NotNull(message = "project interview type cannot be empty")
        EInterviewType interviewType,

        @JsonProperty("feedback_time_range")
        @NotNull(message = "feedback time range cannot be empty")
        EFeedbackTimeRange feedbackTimeRange,

        @JsonProperty("start_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        @NotNull(message = "start date cannot be empty")
        LocalDate startDate,
        @JsonProperty("tags")
        List<String> tags)
{
}
