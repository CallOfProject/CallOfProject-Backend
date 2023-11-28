package callofproject.dev.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ProjectOverviewsDTO(
        @JsonProperty("project_overviews")
        List<ProjectOverviewDTO> projectOverviews)
{
}
