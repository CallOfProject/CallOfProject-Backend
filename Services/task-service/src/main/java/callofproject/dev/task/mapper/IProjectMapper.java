package callofproject.dev.task.mapper;

import callofproject.dev.data.task.entity.Project;
import callofproject.dev.task.dto.ProjectDTO;
import org.mapstruct.Mapper;

/**
 * Mapper interface for mapping between Project and ProjectDTO.
 * It provides a method to convert a Project to a ProjectDTO.
 */
@Mapper(implementationName = "ProjectMapperImpl", componentModel = "spring")
public interface IProjectMapper
{
    /**
     * Maps a Project entity to a ProjectDTO.
     *
     * @param project The Project to map.
     * @return The mapped ProjectDTO.
     */
    ProjectDTO toProjectDTO(Project project);
}
