package callofproject.dev.community.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UserDTO(
        @JsonProperty("user_id")
        UUID userId,
        @JsonProperty("username")
        String username)
{
}
