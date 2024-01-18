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

/**
 * Mapper class for mapping User entities to UserDTOs.
 */
@Mapper(implementationName = "UserMapperImpl", componentModel = "spring")
public interface IUserMapper
{
    /**
     * Maps a UserSignUpRequestDTO to a User entity.
     *
     * @param dto The UserSignUpRequestDTO to be mapped.
     * @return A User entity representing the mapped UserSignUpRequestDTO.
     */
    User toUser(UserSignUpRequestDTO dto);

    /**
     * Maps a User entity to a UserDTO.
     *
     * @param user The User entity to be mapped.
     * @return A UserDTO representing the mapped User entity.
     */
    @Mapping(target = "deletedAt", source = "deleteAt")
    UserShowingAdminDTO toUserShowingAdminDTO(User user);

    /**
     * Maps a User entity to a UserDTO.
     *
     * @param user The User entity to be mapped.
     * @return A UserDTO representing the mapped User entity.
     */
    UserDTO toUserDTO(User user);

    /**
     * Maps a list of User entities to a UsersShowingAdminDTO.
     *
     * @param list The list of User entities to be mapped.
     * @return A UsersDTO representing the mapped list of User entities.
     */
    default UsersShowingAdminDTO toUsersShowingAdminDTO(List<UserShowingAdminDTO> list)
    {
        return new UsersShowingAdminDTO(list);
    }

    /**
     * Maps a list of User entities to a UsersDTO.
     *
     * @param list The list of User entities to be mapped.
     * @return A UsersDTO representing the mapped list of User entities.
     */
    default UsersDTO toUsersDTO(List<UserDTO> list)
    {
        return new UsersDTO(list);
    }
}
