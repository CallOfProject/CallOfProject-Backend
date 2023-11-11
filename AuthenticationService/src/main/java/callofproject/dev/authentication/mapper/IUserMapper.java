package callofproject.dev.authentication.mapper;


import callofproject.dev.authentication.dto.UserShowingAdminDTO;
import callofproject.dev.authentication.dto.UserSignUpRequestDTO;
import callofproject.dev.authentication.dto.UsersShowingAdminDTO;
import callofproject.dev.repository.authentication.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(implementationName = "UserMapperImpl", componentModel = "spring")
public interface IUserMapper
{
    User toUser(UserSignUpRequestDTO dto);

    UserShowingAdminDTO toUserShowingAdminDTO(User user);

    default UsersShowingAdminDTO toUsersShowingAdminDTO(List<UserShowingAdminDTO> list)
    {
        return new UsersShowingAdminDTO(list);
    }
}
