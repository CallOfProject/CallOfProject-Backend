package callofproject.dev.project.dto.overview;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * ProjectOverviewsDTO
 */
public record ProjectOverviewsDTO(
        @JsonProperty("project_overviews")
        List<ProjectOverviewDTO> projectOverviews)
{
}
