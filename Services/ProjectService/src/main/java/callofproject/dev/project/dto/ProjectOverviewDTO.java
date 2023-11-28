package callofproject.dev.project.dto;

import callofproject.dev.nosql.entity.ProjectTag;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record ProjectOverviewDTO(
        @JsonProperty("project_image_path")
        String projectImagePath,
        @JsonProperty("project_title")
        String projectTitle,
        @JsonProperty("project_summary")
        String projectSummary,
        @JsonProperty("project_aim")
        String projectAim,
        @JsonProperty("project_owner_name")
        String projectOwnerName,
        @JsonProperty("techinical_requirements")
        String technicalRequirements,
        @JsonProperty("special_requirements")
        String specialRequirements,
        @JsonProperty("application_deadline")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate applicationDeadline,
        @JsonProperty("expected_completion_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate expectedCompletionDate,
        @JsonProperty("project_tags")
        List<ProjectTag> projectTags
)
{
}
