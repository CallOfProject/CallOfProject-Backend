package callofproject.dev.task.dto.response;

import callofproject.dev.data.task.entity.enums.Priority;
import callofproject.dev.data.task.entity.enums.TaskStatus;
import callofproject.dev.task.dto.ProjectDTO;
import callofproject.dev.task.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class TaskDTO implements Comparable<TaskDTO>
{
    @JsonProperty("task_id")
    private final UUID taskId;
    @JsonProperty("project")
    private final ProjectDTO projectDTO;
    @JsonProperty("title")
    private final String title;
    @JsonProperty("description")
    private final String description;
    @JsonProperty("priority")
    private final Priority priority;
    @JsonProperty("task_status")
    private final TaskStatus taskStatus;
    @JsonProperty("start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private final LocalDate startDate;
    @JsonProperty("end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private final LocalDate endDate;
    @JsonProperty("assignees")
    private final List<UserDTO> assignees;

    public TaskDTO(
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
        this.taskId = taskId;
        this.projectDTO = projectDTO;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.taskStatus = taskStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assignees = assignees;
    }

    @JsonProperty("task_id")
    public UUID taskId()
    {
        return taskId;
    }

    @JsonProperty("project")
    public ProjectDTO projectDTO()
    {
        return projectDTO;
    }

    @JsonProperty("title")
    public String title()
    {
        return title;
    }

    @JsonProperty("description")
    public String description()
    {
        return description;
    }

    @JsonProperty("priority")
    public Priority priority()
    {
        return priority;
    }

    @JsonProperty("task_status")
    public TaskStatus taskStatus()
    {
        return taskStatus;
    }

    @JsonProperty("start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    public LocalDate startDate()
    {
        return startDate;
    }

    @JsonProperty("end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    public LocalDate endDate()
    {
        return endDate;
    }

    @JsonProperty("assignees")
    public List<UserDTO> assignees()
    {
        return assignees;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TaskDTO) obj;
        return Objects.equals(this.taskId, that.taskId) &&
                Objects.equals(this.projectDTO, that.projectDTO) &&
                Objects.equals(this.title, that.title) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.priority, that.priority) &&
                Objects.equals(this.taskStatus, that.taskStatus) &&
                Objects.equals(this.startDate, that.startDate) &&
                Objects.equals(this.endDate, that.endDate) &&
                Objects.equals(this.assignees, that.assignees);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(taskId, projectDTO, title, description, priority, taskStatus, startDate, endDate, assignees);
    }

    @Override
    public String toString()
    {
        return "TaskDTO[" +
                "taskId=" + taskId + ", " +
                "projectDTO=" + projectDTO + ", " +
                "title=" + title + ", " +
                "description=" + description + ", " +
                "priority=" + priority + ", " +
                "taskStatus=" + taskStatus + ", " +
                "startDate=" + startDate + ", " +
                "endDate=" + endDate + ", " +
                "assignees=" + assignees + ']';
    }


    @Override
    public int compareTo(TaskDTO o)
    {
        return Integer.compare(o.priority.ordinal(), priority.ordinal());
    }
}
