package callofproject.dev.task.mapper;

import callofproject.dev.data.task.entity.Project;
import callofproject.dev.data.task.entity.Task;
import callofproject.dev.data.task.entity.User;
import callofproject.dev.task.dto.request.CreateTaskDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(implementationName = "TaskMapperImpl", componentModel = "spring")
public interface ITaskMapper
{
    @Mapping(target="project", source="project")
    @Mapping(target="assignees", source="assignees")
    Task toTask(CreateTaskDTO createTaskDTO, Project project, Set<User> assignees);
}
