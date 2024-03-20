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
 * Create Task DTO
 * <p>
 * This class contains the create task data transfer object
 */
public record CreateTaskDTO(
        @JsonProperty("project_id")
        @NotNull(message = "project id cannot be empty")
        UUID projectId,

        @JsonProperty("title")
        @NotNull(message = "title cannot be empty")
        @NotEmpty(message = "title cannot be empty")
        String title,

        @JsonProperty("description")
        @NotNull(message = "description cannot be empty")
        @NotEmpty(message = "description cannot be empty")
        String description,

        @JsonProperty("user_ids")
        @NotNull(message = "user ids cannot be empty")
        @NotEmpty(message = "user ids cannot be empty")
        List<UUID> userIds,

        @JsonProperty("priority")
        @NotNull(message = "priority cannot be empty")
        Priority priority,

        @JsonProperty("task_status")
        @NotNull(message = "task status cannot be empty")
        TaskStatus taskStatus,

        @JsonProperty("start_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        @NotNull(message = "start date cannot be empty")
        LocalDate startDate,

        @JsonProperty("end_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        @NotNull(message = "end date cannot be empty")
        LocalDate endDate)
{
}
