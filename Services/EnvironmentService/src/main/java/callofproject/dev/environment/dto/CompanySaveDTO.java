package callofproject.dev.environment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CompanySaveDTO(
        @JsonProperty("company_name")
        String companyName
)
{
}
