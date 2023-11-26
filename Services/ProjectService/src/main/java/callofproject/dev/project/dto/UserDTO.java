package callofproject.dev.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record UserDTO(
        String username,
        String email,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("middle_name")
        String middleName,
        @JsonProperty("last_name")
        String lastName
)
{
}
