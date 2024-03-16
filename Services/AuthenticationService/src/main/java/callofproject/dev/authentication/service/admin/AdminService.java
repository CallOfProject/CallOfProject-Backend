package callofproject.dev.authentication.service.admin;

import callofproject.dev.authentication.dto.admin.UserShowingAdminDTO;
import callofproject.dev.authentication.dto.admin.UserUpdateDTOAdmin;
import callofproject.dev.authentication.dto.admin.UsersShowingAdminDTO;
import callofproject.dev.authentication.dto.auth.AuthenticationRequest;
import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.library.exception.service.DataServiceException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;

/**
 * Service class for admin-related operations.
 * It implements the IAdminService interface.
 */
@Service
@Lazy
public class AdminService implements IAdminService
{
    private final AdminServiceCallback m_serviceCallback;

    /**
     * Constructor for AdminService class.
     *
     * @param serviceCallback The AdminServiceCallback object to be injected.
     */
    public AdminService(AdminServiceCallback serviceCallback)
    {
        m_serviceCallback = serviceCallback;
    }

    /**
     * Find all users pageable
     *
     * @param page represent the page
     * @return the UsersShowingAdminDTO
     */
    @Override
    public MultipleResponseMessagePageable<UsersShowingAdminDTO> findAllUsersPageable(int page)
    {
        return doForDataService(() -> m_serviceCallback.findAllUsersPageable(page), "AdminService::findAllUsersPageable");
    }


    /**
     * Find all users pageable
     *
     * @return the UsersShowingAdminDTO
     */
    @Override
    public MultipleResponseMessage<UsersShowingAdminDTO> findAllUsers()
    {
        return doForDataService(m_serviceCallback::findAllUsers, "AdminService::findAllUsersPageable");
    }


    /**
     * Remove user with given username
     *
     * @param username represent the username
     * @return boolean value.
     */
    @Override
    public ResponseMessage<Boolean> removeUser(String username)
    {
        var removedUser = doForDataService(() -> m_serviceCallback.removeUser(username), "AdminService::removeUser");

        if (removedUser.getObject())
            m_serviceCallback.publishUser(username, EOperation.DELETE);

        return removedUser;
    }

    /**
     * Find Users with given word. If username contains the word, return it.
     *
     * @param page represent the page
     * @param word represent the part of username
     * @return UsersShowingAdminDTO
     */
    @Override
    public MultipleResponseMessagePageable<UsersShowingAdminDTO> findUsersByUsernameContainsIgnoreCase(int page, String word)
    {
        return doForDataService(() -> m_serviceCallback.findUsersByUsernameContainsIgnoreCase(page, word),
                "AdminService::findUsersByUsernameContainsIgnoreCase");
    }

    /**
     * Find Users with given word. If username not contains the word, return it.
     *
     * @param page represent the page
     * @param word represent the part of username
     * @return UsersShowingAdminDTO
     */
    @Override
    public MultipleResponseMessagePageable<UsersShowingAdminDTO> findUsersByUsernameNotContainsIgnoreCase(int page, String word)
    {
        return doForDataService(() -> m_serviceCallback.findUsersByUsernameNotContainsIgnoreCase(page, word), "AdminService::findUsersByUsernameNotContainsIgnoreCase");
    }


    /**
     * Update user with given UserUpdateDTOAdmin class
     *
     * @param userUpdateDTO represent the updating information
     * @return UserShowingAdminDTO class.
     */
    @Override
    public ResponseMessage<UserShowingAdminDTO> updateUser(UserUpdateDTOAdmin userUpdateDTO)
    {
        var updatedUser = doForDataService(() -> m_serviceCallback.updateUser(userUpdateDTO), "AdminService::updateUser");

        if (updatedUser.getObject() != null)
            m_serviceCallback.publishUser(updatedUser.getObject().userId(), EOperation.UPDATE);

        return updatedUser;
    }


    /**
     * Find total user count.
     *
     * @return the total user count
     */
    @Override
    public long findAllUserCount()
    {
        return doForDataService(m_serviceCallback::findAllUserCount, "AdminService::findAllUserCount");
    }

    /**
     * Find new users last n day.
     *
     * @param day represent the day
     * @return new user count.
     */
    @Override
    public long findNewUsersLastNday(long day)
    {
        return doForDataService(() -> m_serviceCallback.findNewUsersLastNday(day), "AdminService::findNewUsersLastNday");
    }


    /**
     * Authenticates a user based on their username and password.
     *
     * @param request The authentication request containing the user's credentials.
     * @return An AuthenticationResponse containing the JWT token and other authentication details.
     * @throws DataServiceException if the user is not an admin or if authentication fails.
     */
    @Override
    public Object authenticate(AuthenticationRequest request)
    {
        return m_serviceCallback.authenticate(request);
    }
}