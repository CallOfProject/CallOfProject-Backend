package callofproject.dev.task.dto.request;

import callofproject.dev.data.task.entity.enums.Priority;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChangeTaskPriorityDTO(
        @JsonProperty("task_id")
        @NotNull(message = "task id cannot be empty")
        UUID taskId,
        @JsonProperty("user_id")
        @NotNull(message = "user id cannot be empty")
        UUID userId,
        @JsonProperty("priority")
        @NotNull(message = "priority cannot be empty")
        Priority priority)
{
}
