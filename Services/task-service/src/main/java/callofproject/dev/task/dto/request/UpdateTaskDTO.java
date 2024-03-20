package callofproject.dev.task.dto.request;

import callofproject.dev.data.task.entity.enums.Priority;
import callofproject.dev.data.task.entity.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Update Task DTO
 * <p>
 * This class contains the update task data transfer object
 */
public record UpdateTaskDTO(
        @JsonProperty("task_id")
        @NotNull(message = "task id cannot be empty")
        UUID taskId,
        @JsonProperty("owner_id")
        @NotNull(message = "owner id cannot be empty")
        UUID ownerId,
        @JsonProperty("title")
        @NotNull(message = "title cannot be empty")
        @NotEmpty(message = "title cannot be empty")
        String title,
        @JsonProperty("description")
        @NotNull(message = "description cannot be empty")
        @NotEmpty(message = "description cannot be empty")
        String description,
        @JsonProperty("priority")
        @NotNull(message = "priority cannot be empty")
        Priority priority,
        @JsonProperty("task_status")
        @NotNull(message = "task status cannot be empty")
        TaskStatus taskStatus,
        @JsonProperty("user_ids")
        @NotNull(message = "user ids cannot be empty")
        @NotEmpty(message = "user ids cannot be empty")
        List<UUID> userIds,
        @JsonProperty("start_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        @NotNull(message = "Start date cannot be empty")
        LocalDate startDate,
        @JsonProperty("end_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        @NotNull(message = "End date cannot be empty")
        LocalDate endDate)
{
}
