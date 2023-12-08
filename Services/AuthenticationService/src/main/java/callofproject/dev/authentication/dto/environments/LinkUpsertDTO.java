package callofproject.dev.authentication.dto.environments;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.UUID;

public record LinkUpsertDTO(
        @JsonProperty("user_id")
        @Tag(name = "user_id", description = "type: UUID")
        UUID userId,
        @JsonProperty("link_title")
        String linkTitle,
        String link)
{
}
