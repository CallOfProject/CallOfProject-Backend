package callofproject.dev.task.mapper;

import org.mapstruct.Mapper;

@Mapper(implementationName = "ProjectMapperImpl", componentModel = "spring")
public interface IProjectMapper
{
    /*@Mappings({
            @Mapping(target = "tasks", source = "tasks")
    })
    ProjectDTO toProjectDTO(Project project);*/
}
