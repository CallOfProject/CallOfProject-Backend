package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.UserDTO;
import callofproject.dev.authentication.dto.UserSignUpRequestDTO;
import callofproject.dev.authentication.dto.UsersDTO;
import callofproject.dev.authentication.dto.admin.UserShowingAdminDTO;
import callofproject.dev.authentication.dto.admin.UsersShowingAdminDTO;
import callofproject.dev.repository.authentication.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(implementationName = "UserMapperImpl", componentModel = "spring")
public interface IUserMapper
{
    User toUser(UserSignUpRequestDTO dto);

    @Mapping(target="deletedAt", source = "deleteAt")
    UserShowingAdminDTO toUserShowingAdminDTO(User user);

    UserDTO toUserDTO(User user);

    default UsersShowingAdminDTO toUsersShowingAdminDTO(List<UserShowingAdminDTO> list)
    {
        return new UsersShowingAdminDTO(list);
    }

    default UsersDTO toUsersDTO(List<UserDTO> list)
    {
        return new UsersDTO(list);
    }
}
