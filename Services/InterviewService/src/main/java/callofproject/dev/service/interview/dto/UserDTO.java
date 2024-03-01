package callofproject.dev.service.interview.dto;

import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.service.interview.data.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


/**
 * UserDTO
 */
public record UserDTO(
        @JsonProperty("user_id")
        UUID userId,
        String username,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("middle_name")
        String middleName,
        @JsonProperty("last_name")
        String lastName,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDateTime deletedAt
)
{
}
