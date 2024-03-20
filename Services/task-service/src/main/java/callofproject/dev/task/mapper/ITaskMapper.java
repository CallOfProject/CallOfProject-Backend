package callofproject.dev.task.mapper;

import callofproject.dev.data.task.entity.Project;
import callofproject.dev.data.task.entity.Task;
import callofproject.dev.data.task.entity.User;
import callofproject.dev.task.dto.ProjectDTO;
import callofproject.dev.task.dto.UserDTO;
import callofproject.dev.task.dto.request.CreateTaskDTO;
import callofproject.dev.task.dto.response.TaskDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

/**
 * Mapper interface for mapping between CreateTaskDTO, Task, TaskDTO and User entity.
 * It provides a method to convert a CreateTaskDTO to a Task entity and a Task entity to a TaskDTO.
 */
@Mapper(implementationName = "TaskMapperImpl", componentModel = "spring", uses = {ProjectDTO.class, UserDTO.class})
public interface ITaskMapper
{
    /**
     * Maps a CreateTaskDTO to a Task entity.
     *
     * @param createTaskDTO The CreateTaskDTO to map.
     * @param project       The Project to map.
     * @param assignees     The User to map.
     * @return The mapped Task entity.
     */
    @Mapping(target = "project", source = "project")
    @Mapping(target = "assignees", source = "assignees")
    Task toTask(CreateTaskDTO createTaskDTO, Project project, Set<User> assignees);

    /**
     * Maps a Task entity to a TaskDTO.
     *
     * @param task      The Task to map.
     * @param project   The ProjectDTO to map.
     * @param assignees The UserDTO to map.
     * @return The mapped TaskDTO.
     */
    @Mapping(target = "projectDTO", source = "project")
    @Mapping(target = "assignees", source = "assignees")
    TaskDTO toTaskDTO(Task task, ProjectDTO project, List<UserDTO> assignees);
}
