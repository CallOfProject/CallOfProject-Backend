package callofproject.dev.authentication.dto;

import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.repository.authentication.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;
import java.util.UUID;

public record UserKafkaDTO(
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
