package callofproject.dev.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record UserTagCreateDTO(
        @JsonProperty("tag_names")
        List<String> tagNames,
        @JsonProperty("user_id")
        UUID userId
)
{
}
