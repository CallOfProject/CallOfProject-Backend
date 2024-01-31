package callofproject.dev.task.dto.response;

import callofproject.dev.data.task.entity.enums.Priority;
import callofproject.dev.data.task.entity.enums.TaskStatus;
import callofproject.dev.task.dto.ProjectDTO;
import callofproject.dev.task.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TaskDTO(
        @JsonProperty("task_id")
        UUID taskId,

        @JsonProperty("project")
        ProjectDTO projectDTO,

        @JsonProperty("title")
        String title,

        @JsonProperty("description")
        String description,

        @JsonProperty("priority")
        Priority priority,

        @JsonProperty("task_status")
        TaskStatus taskStatus,

        @JsonProperty("start_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate startDate,

        @JsonProperty("end_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate endDate,

        @JsonProperty("assignees")
        List<UserDTO> assignees)
{

}
