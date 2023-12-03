package callofproject.dev.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LinkUpsertDTO(
        @JsonProperty("link_title")
        String linkTitle,
        String link)
{
}
