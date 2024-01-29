package callofproject.dev.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UserDTO(
        @JsonProperty("user_id")
        UUID userId,
        @JsonProperty("username")
        String username,
        @JsonProperty("email")
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
