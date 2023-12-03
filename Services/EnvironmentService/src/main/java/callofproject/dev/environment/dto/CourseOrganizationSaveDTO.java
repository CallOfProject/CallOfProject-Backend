package callofproject.dev.environment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CourseOrganizationSaveDTO(
        @JsonProperty("organization_name")
        String courseName)
{
}
