package callofproject.dev.task.dto;

import callofproject.dev.data.task.entity.enums.AdminOperationStatus;
import callofproject.dev.data.task.entity.enums.EProjectStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Project DTO
 * <p>
 * This class contains the project data transfer object
 */
public record ProjectDTO(
        @JsonProperty("project_id")
        UUID projectId,
        @JsonProperty("project_name")
        String projectName,
        @JsonProperty("project_owner")
        UserDTO projectOwner,
        @JsonProperty("project_status")
        EProjectStatus projectStatus,
        @JsonProperty("admin_operation_status")
        AdminOperationStatus adminOperationStatus)
{
}
