package callofproject.dev.project.dto;


import callofproject.dev.data.project.entity.enums.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;


public record ProjectAdminDTO(
        @JsonProperty("project_id")
        UUID projectId,
        @JsonProperty("project_owner")
        String projectOwnerUsername,
        @JsonProperty("project_image_path")
        String projectImagePath,
        @JsonProperty("project_name")
        String projectName,
        @JsonProperty("project_summary")
        String projectSummary,
        @JsonProperty("description")
        String description,
        @JsonProperty("project_aim")
        String projectAim,
        @JsonProperty("project_status")
        EProjectStatus projectStatus,
        @JsonProperty("project_access_type")
        EProjectAccessType projectAccessType,
        @JsonProperty("profession_level")
        EProjectProfessionLevel professionLevel,
        @JsonProperty("degree")
        EDegree degree,
        @JsonProperty("project_level")
        EProjectLevel projectLevel,
        @JsonProperty("start_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate startDate,
        @JsonProperty("expected_completion_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate expectedCompletionDate,
        @JsonProperty("application_deadline")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate applicationDeadline,
        @JsonProperty("completion_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate completionDate,
        @JsonProperty("max_participants")
        int maxParticipants,
        @JsonProperty("current_participants")
        int currentParticipants
)
{
}
