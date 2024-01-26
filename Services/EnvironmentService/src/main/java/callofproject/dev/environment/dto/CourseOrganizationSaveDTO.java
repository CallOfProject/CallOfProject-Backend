package callofproject.dev.environment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CourseOrganizationSaveDTO
 */
public record CourseOrganizationSaveDTO(
        @JsonProperty("organization_name")
        String courseName)
{
}
