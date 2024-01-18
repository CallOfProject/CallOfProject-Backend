package callofproject.dev.authentication.dto.environments;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Data Transfer Object for a link.
 */
public record LinkUpsertDTO(
        @JsonProperty("user_id")
        @Tag(name = "user_id", description = "type: UUID")
        @NotNull(message = "User ID cannot be null")
        UUID userId,
        @JsonProperty("link_title")
        @NotBlank(message = "Link title cannot be blank")
        @Size(max = 255, message = "Link title cannot exceed 255 characters")
        String linkTitle,
        @NotBlank(message = "Link cannot be blank")
        String link)
{
}
