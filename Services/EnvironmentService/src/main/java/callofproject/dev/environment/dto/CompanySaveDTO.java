package callofproject.dev.environment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CompanySaveDTO
 */
public record CompanySaveDTO(
        @JsonProperty("company_name")
        String companyName
)
{
}
