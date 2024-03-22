package callofproject.dev.task;

import callofproject.dev.data.task.entity.enums.Priority;
import callofproject.dev.data.task.entity.enums.TaskStatus;
import callofproject.dev.task.dto.request.CreateTaskDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class TestUtil
{

    private TestUtil()
    {
    }

    public static CreateTaskDTO provideCreateTaskDTO(UUID projectId, String title, String description, List<UUID> userIds, Priority priority, TaskStatus taskStatus, LocalDate startDate, LocalDate endDate)
    {
        return new CreateTaskDTO(projectId, title, description, userIds, priority, taskStatus, startDate, endDate);
    }
}
