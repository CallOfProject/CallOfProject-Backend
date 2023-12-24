package callofproject.dev.project.dto;

import callofproject.dev.data.common.enums.EOperation;
import com.fasterxml.jackson.annotation.JsonProperty;

import callofproject.dev.data.project.entity.Role;
import java.util.Set;
import java.util.UUID;


public record UserDTO(
        @JsonProperty("user_id")
        UUID userId,
        String username,
        String email,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("middle_name")
        String middleName,
        @JsonProperty("last_name")
        String lastName,
        EOperation operation,
        String password,
        Set<Role> roles,
        int ownerProjectCount,
        int participantProjectCount,
        int totalProjectCount
)
{
}
