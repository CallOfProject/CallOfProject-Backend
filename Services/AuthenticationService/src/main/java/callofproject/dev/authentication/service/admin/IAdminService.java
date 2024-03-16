package callofproject.dev.authentication.service.admin;

import callofproject.dev.authentication.dto.admin.UserShowingAdminDTO;
import callofproject.dev.authentication.dto.admin.UserUpdateDTOAdmin;
import callofproject.dev.authentication.dto.admin.UsersShowingAdminDTO;
import callofproject.dev.authentication.dto.auth.AuthenticationRequest;
import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;

public interface IAdminService
{
    ResponseMessage<Boolean> removeUser(String username);

    MultipleResponseMessagePageable<UsersShowingAdminDTO> findAllUsersPageable(int page);

    MultipleResponseMessage<UsersShowingAdminDTO> findAllUsers();

    MultipleResponseMessagePageable<UsersShowingAdminDTO> findUsersByUsernameContainsIgnoreCase(int page, String word);

    MultipleResponseMessagePageable<UsersShowingAdminDTO> findUsersByUsernameNotContainsIgnoreCase(int page, String word);

    ResponseMessage<UserShowingAdminDTO> updateUser(UserUpdateDTOAdmin userUpdateDTO);

    Object authenticate(AuthenticationRequest request);

    long findAllUserCount();

    long findNewUsersLastNday(long day);
}
