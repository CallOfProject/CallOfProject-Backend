package callofproject.dev.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserTagDTO(
        @JsonProperty("tag_name")
        String tagName,
        @JsonProperty("tag_id")
        String tagId
)
{
}
