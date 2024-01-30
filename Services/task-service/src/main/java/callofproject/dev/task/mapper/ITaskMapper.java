package callofproject.dev.task.mapper;

import callofproject.dev.data.task.entity.Project;
import callofproject.dev.data.task.entity.Task;
import callofproject.dev.data.task.entity.User;
import callofproject.dev.task.dto.ProjectDTO;
import callofproject.dev.task.dto.TaskDTO;
import callofproject.dev.task.dto.UserDTO;
import callofproject.dev.task.dto.request.CreateTaskDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(implementationName = "TaskMapperImpl", componentModel = "spring", uses = {ProjectDTO.class, UserDTO.class})
public interface ITaskMapper
{
    @Mapping(target = "project", source = "project")
    @Mapping(target = "assignees", source = "assignees")
    Task toTask(CreateTaskDTO createTaskDTO, Project project, Set<User> assignees);

    @Mapping(target = "projectDTO", source = "project")
    @Mapping(target = "assignees", source = "assignees")
    TaskDTO toTaskDTO(Task task, ProjectDTO project, List<UserDTO> assignees);
}
