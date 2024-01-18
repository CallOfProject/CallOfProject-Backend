package callofproject.dev.authentication.dto.user_profile;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object for a link.
 */
public record LinkDTO(
        long linkId,
        @JsonProperty("link_title")
        String linkTitle,
        String link)
{
}
