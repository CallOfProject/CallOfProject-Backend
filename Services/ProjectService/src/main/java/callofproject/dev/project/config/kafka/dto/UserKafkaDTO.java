package callofproject.dev.project.config.kafka.dto;

import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.data.project.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


/**
 * UserDTO
 */
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
        LocalDateTime deletedAt,
        int ownerProjectCount,
        int participantProjectCount,
        int totalProjectCount
)
{
}
