package callofproject.dev.authentication.dto.user_profile;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LinkDTO(
        long linkId,
        @JsonProperty("link_title")
        String linkTitle,
        String link)
{
}
