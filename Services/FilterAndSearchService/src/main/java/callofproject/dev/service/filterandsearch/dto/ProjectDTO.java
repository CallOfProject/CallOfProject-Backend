package callofproject.dev.service.filterandsearch.dto;

import callofproject.dev.data.project.entity.enums.EProjectStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

public record ProjectDTO(
        @JsonProperty("project_id")
        UUID projectId,

        @JsonProperty("project_name")
        String projectName,

        @JsonProperty("project_image")
        String projectImage,

        @JsonProperty("project_summary")
        String projectSummary,

        @JsonProperty("project_owner")
        String projectOwner,

        @JsonProperty("project_status")
        EProjectStatus projectStatus,

        @JsonProperty("application_deadline")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate applicationDeadline,

        @JsonProperty("creation_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate creationDate,

        @JsonProperty("start_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate startDate,

        @JsonProperty("expected_completion_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate expectedCompletionDate
)
{
}
