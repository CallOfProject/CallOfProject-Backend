package callofproject.dev.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UserTagUpdateDTO(
        @JsonProperty("user_id")
        UUID userId,
        @JsonProperty("tag_id")
        UUID tagId,
        @JsonProperty("tag_name")
        String tagName
)
{
}
