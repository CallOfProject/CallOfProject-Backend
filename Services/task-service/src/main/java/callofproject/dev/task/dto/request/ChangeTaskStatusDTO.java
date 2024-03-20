package callofproject.dev.task.dto.request;

import callofproject.dev.data.task.entity.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Change Task Status DTO
 * <p>
 * This class contains the change task status data transfer object
 */
public record ChangeTaskStatusDTO(
        @JsonProperty("task_id")
        @NotNull(message = "task id cannot be empty")
        UUID taskId,
        @JsonProperty("user_id")
        @NotNull(message = "user id cannot be empty")
        UUID userId,
        @JsonProperty("status")
        @NotNull(message = "status cannot be empty")
        TaskStatus status)
{
}
