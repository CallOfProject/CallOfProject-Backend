package callofproject.dev.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ProjectDTO(
        @JsonProperty("project_id")
        UUID projectId,
        @JsonProperty("project_name")
        String projectName,
        @JsonProperty("project_owner")
        UserDTO projectOwner
)
{
}
