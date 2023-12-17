package callofproject.dev.project.dto.overview;

import callofproject.dev.data.project.entity.enums.*;
import callofproject.dev.nosql.entity.ProjectTag;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record ProjectOverviewDTO(
        @JsonProperty("project_id")
        String projectId,
        @JsonProperty("project_image_path")
        String projectImagePath,
        @JsonProperty("project_title")
        String projectTitle,
        @JsonProperty("project_summary")
        String projectSummary,
        @JsonProperty("project_aim")
        String projectAim,
        @JsonProperty("application_deadline")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate applicationDeadline,
        @JsonProperty("expected_completion_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate expectedCompletionDate,
        @JsonProperty("start_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate startDate,
        @JsonProperty("max_participant")
        int maxParticipant,
        @JsonProperty("techinical_requirements")
        String technicalRequirements,
        @JsonProperty("special_requirements")
        String specialRequirements,
        @JsonProperty("project_profession_level")
        EProjectProfessionLevel professionLevel,
        @JsonProperty("project_degree")
        EDegree degree,
        @JsonProperty("project_level")
        EProjectLevel projectLevel,
        @JsonProperty("interview_type")
        EInterviewType interviewType,
        @JsonProperty("project_owner_name")
        String projectOwnerName,
        @JsonProperty("feedback_time_range")
        EFeedbackTimeRange feedbackTimeRange,
        @JsonProperty("project_status")
        EProjectStatus projectStatus,
        @JsonProperty("project_tags")
        List<ProjectTag> projectTags


)
{
}
