package callofproject.dev.project.dto;


import callofproject.dev.data.project.entity.enums.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

public record ProjectSaveDTO(
        @JsonProperty("user_id")
        UUID userId,
        @JsonProperty("project_image")
        String projectImage,
        @JsonProperty("project_name")
        String projectName,
        @JsonProperty("project_summary")
        String projectSummary,
        @JsonProperty("project_description")
        String projectDescription,
        @JsonProperty("project_aim")
        String projectAim,
        @JsonProperty("project_deadline")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate applicationDeadline,
        @JsonProperty("expected_completion_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate expectedCompletionDate,
        @JsonProperty("expected_project_deadline")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate expectedProjectDeadline,
        @JsonProperty("max_participant_count")
        int maxParticipantCount,
        @JsonProperty("technical_requirements")
        String technicalRequirements,
        @JsonProperty("special_requirements")
        String specialRequirements,
        @JsonProperty("project_access_type")
        EProjectAccessType projectAccessType,
        @JsonProperty("project_profession_level")
        EProjectProfessionLevel professionLevel,
        @JsonProperty("project_sector")
        ESector sector,
        @JsonProperty("project_degree")
        EDegree degree,
        @JsonProperty("project_level")
        EProjectLevel projectLevel,
        @JsonProperty("project_interview_type")
        EInterviewType interviewType)
{

}
