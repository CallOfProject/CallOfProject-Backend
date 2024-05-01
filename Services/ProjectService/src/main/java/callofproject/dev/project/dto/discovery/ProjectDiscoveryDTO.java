package callofproject.dev.project.dto.discovery;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

/**
 * ProjectOverviewDTO
 */
public record ProjectDiscoveryDTO(
        @JsonProperty("project_id")
        UUID projectId,
        @JsonProperty("project_image_path")
        String projectImagePath,
        @JsonProperty("project_title")
        String projectTitle,
        @JsonProperty("project_summary")
        String projectSummary,
        @JsonProperty("project_owner")
        String projectOwnerName,
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
