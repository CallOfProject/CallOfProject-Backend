package callofproject.dev.project.mapper;

import callofproject.dev.data.project.entity.Role;
import callofproject.dev.project.dto.RolesDTO;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface RoleMapper
{
    List<RolesDTO> rolesToRoleDTOs(Set<Role> roles);
}
