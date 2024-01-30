package callofproject.dev.task.dto.request;

import callofproject.dev.data.task.entity.enums.Priority;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ChangeTaskPriorityDTO(
        @JsonProperty("task_id")
        UUID taskId,
        @JsonProperty("user_id")
        UUID userId,
        @JsonProperty("priority")
        Priority priority)
{
}
