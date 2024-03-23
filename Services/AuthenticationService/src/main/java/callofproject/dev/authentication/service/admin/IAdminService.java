package callofproject.dev.authentication.service.admin;

import callofproject.dev.authentication.dto.admin.UserShowingAdminDTO;
import callofproject.dev.authentication.dto.admin.UserUpdateDTOAdmin;
import callofproject.dev.authentication.dto.admin.UsersShowingAdminDTO;
import callofproject.dev.authentication.dto.auth.AuthenticationRequest;
import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;

/**
 * The interface Admin service.
 */
public interface IAdminService
{
    /**
     * Remove user response message.
     *
     * @param username the username
     * @return the response message
     */
    ResponseMessage<Boolean> removeUser(String username);

    /**
     * Find all users pageable multiple response message pageable.
     *
     * @param page the page
     * @return the multiple response message pageable
     */
    MultipleResponseMessagePageable<UsersShowingAdminDTO> findAllUsersPageable(int page);

    /**
     * Find all users multiple response message.
     *
     * @return the multiple response message
     */
    MultipleResponseMessage<UsersShowingAdminDTO> findAllUsers();

    /**
     * Find users by username contains ignore case multiple response message pageable.
     *
     * @param page the page
     * @param word the word
     * @return the multiple response message pageable
     */
    MultipleResponseMessagePageable<UsersShowingAdminDTO> findUsersByUsernameContainsIgnoreCase(int page, String word);

    /**
     * Find users by username not contains ignore case multiple response message pageable.
     *
     * @param page the page
     * @param word the word
     * @return the multiple response message pageable
     */
    MultipleResponseMessagePageable<UsersShowingAdminDTO> findUsersByUsernameNotContainsIgnoreCase(int page, String word);

    /**
     * Update user.
     *
     * @param userUpdateDTO the user update dto
     * @return the multiple response message pageable
     */
    ResponseMessage<UserShowingAdminDTO> updateUser(UserUpdateDTOAdmin userUpdateDTO);

    /**
     * Authenticate object.
     *
     * @param request the request
     * @return the object
     */
    Object authenticate(AuthenticationRequest request);

    /**
     * Find all user count long.
     *
     * @return the long
     */
    long findAllUserCount();

    /**
     * Find new users last nday long.
     *
     * @param day the day
     * @return the long
     */
    long findNewUsersLastNday(long day);
}
