package callofproject.dev.task.dto.request;

import callofproject.dev.data.task.entity.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ChangeTaskStatusDTO(
        @JsonProperty("task_id")
        UUID taskId,
        @JsonProperty("user_id")
        UUID userId,
        @JsonProperty("status")
        TaskStatus status)
{
}
