package callofproject.dev.authentication.service.usermanagement;


import callofproject.dev.authentication.config.kafka.KafkaProducer;
import callofproject.dev.authentication.dto.*;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.repository.authentication.entity.UserProfile;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static callofproject.dev.authentication.util.AuthenticationServiceBeanName.USER_MANAGEMENT_SERVICE;
import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;

/**
 * Service class for managing users.
 * It implements the IUserManagementService interface.
 */
@Service(USER_MANAGEMENT_SERVICE)
@Lazy
public class UserManagementService implements IUserManagementService
{
    private final UserManagementServiceCallback m_serviceCallback;
    private final KafkaProducer m_kafkaProducer;

    /**
     * Instantiates a new User management service.
     *
     * @param serviceCallback the service callback
     * @param kafkaProducer   the kafka producer
     */
    public UserManagementService(UserManagementServiceCallback serviceCallback, KafkaProducer kafkaProducer)
    {
        m_serviceCallback = serviceCallback;
        m_kafkaProducer = kafkaProducer;
    }


    /**
     * Save User with given dto class.
     *
     * @param userDTO represent the dto class
     * @return UserSaveDTO.
     */
    @Override
    public ResponseMessage<UserSaveDTO> saveUser(UserSignUpRequestDTO userDTO)
    {
        var result = doForDataService(() -> m_serviceCallback.saveUserCallback(userDTO), "User cannot be saved!");

        if (result.getStatusCode() == 200)
            m_serviceCallback.publishUserForCreate(result.getObject().userId());

        return result;
    }


    @Override
    public ResponseMessage<UserSaveDTO> saveUserForMobile(UserSignUpRequestDTO userDTO)
    {
        var result = doForDataService(() -> m_serviceCallback.saveUserForMobileCallback(userDTO), "User cannot be saved!");

        if (result.getStatusCode() == 200)
            m_serviceCallback.publishUserForUpdate(result.getObject().userId());

        return result;
    }


    /**
     * Find user with given username
     *
     * @param username represent the username.
     * @return UserDTO class.
     */
    @Override
    public ResponseMessage<UserDTO> findUserByUsername(String username)
    {
        return doForDataService(() -> m_serviceCallback.findUserByUsernameCallback(username), "User does not exists!");
    }

    /**
     * Find User with given username but returns the user entity.
     *
     * @param username represent the username.
     * @return User class.
     */
    @Override
    public UserResponseDTO<User> findUserByUsernameForAuthenticationService(String username)
    {
        return doForDataService(() -> m_serviceCallback.findUserByUsernameForAuthenticationServiceCallback(username), "User does not exists!");
    }

    /**
     * Find all users with given word and page.
     *
     * @param page represent the page.
     * @param word represent the containing word.
     * @return UsersDTO class.
     */
    @Override
    public MultipleResponseMessagePageable<Object> findAllUsersPageableByContainsWord(int page, String word)
    {
        return doForDataService(() -> m_serviceCallback.findAllUsersPageableByContainsWordCallback(page, word), "UserManagementService::findAllUsersPageableByContainsWord");
    }


    /**
     * Update user profile with given dto class.
     *
     * @param dto  represent the dto class
     * @param file represent the file
     * @param cv   represent the cv
     * @return MessageResponseDTO.
     */
    @Override
    public ResponseMessage<Object> upsertUserProfile(UserProfileUpdateDTO dto, MultipartFile file, MultipartFile cv)
    {
        return doForDataService(() -> m_serviceCallback.upsertUserProfileCallback(dto, file, cv), "UserManagementService::upsertUserProfile");
    }

    /**
     * Find user profile with given user id.
     *
     * @param userId represent the user id.
     * @return UserProfileDTO class.
     */
    @Override
    public ResponseMessage<Object> findUserProfileByUserId(UUID userId)
    {
        return doForDataService(() -> m_serviceCallback.findUserProfileByUserIdCallback(userId), "UserManagementService::findUserProfileByUserId");
    }

    /**
     * Find user profile with given username.
     *
     * @param username represent the username.
     * @return UserProfileDTO class.
     */
    @Override
    public ResponseMessage<Object> findUserProfileByUsername(String username)
    {
        return doForDataService(() -> m_serviceCallback.findUserProfileByUserUsernameCallback(username), "UserManagementService::findUserProfileByUserId");
    }

    /**
     * Find user and his/her profile with given id.
     *
     * @param userId represent the user id.
     * @return UserWithProfileDTO class.
     */
    @Override
    public ResponseMessage<Object> findUserWithProfile(UUID userId)
    {
        return doForDataService(() -> m_serviceCallback.findUserWithProfileCallback(userId), "UserManagementService::findUserWithProfile");
    }

    @Override
    public ResponseMessage<Object> updateAboutMe(UUID userId, String aboutMe)
    {
        return doForDataService(() -> m_serviceCallback.updateAboutMeCallback(userId, aboutMe), "UserManagementService::updateAboutMe");
    }

    @Override
    public ResponseMessage<Object> uploadUserProfilePhoto(UUID userId, MultipartFile file)
    {
        var profile = doForDataService(() -> m_serviceCallback.uploadUserProfilePhotoCallback(userId, file),
                "UserManagementService::uploadUserProfilePhoto");

        if (profile.getStatusCode() == 200)
            m_serviceCallback.publishUserForCreate(userId);
        return new ResponseMessage<>(profile.getMessage(), profile.getStatusCode(), ((UserProfile) profile.getObject()).getProfilePhoto());
    }

    @Override
    public ResponseMessage<Object> uploadUserCv(UUID userId, MultipartFile file)
    {
        return doForDataService(() -> m_serviceCallback.uploadUserCvCallback(userId, file), "UserManagementService::uploadUserCv");
    }

    @Override
    public ResponseMessage<Object> createUserTag(UserTagCreateDTO dto)
    {
        return doForDataService(() -> m_serviceCallback.createUserTagCallback(dto), "UserManagementService::createUserTag");
    }

    @Override
    public ResponseMessage<Object> updateUserTag(UserTagUpdateDTO dto)
    {
        return doForDataService(() -> m_serviceCallback.updateUserTagCallback(dto), "UserManagementService::updateUserTag");
    }

    @Override
    public ResponseMessage<Object> deleteUserTag(UUID userId, UUID tagId)
    {
        return doForDataService(() -> m_serviceCallback.deleteUserTagCallback(userId, tagId), "UserManagementService::deleteUserTag");
    }

    @Override
    public ResponseMessage<Object> createUserTag(String tagName, UUID userId)
    {
        return doForDataService(() -> m_serviceCallback.createUserTagCallback(tagName, userId), "UserManagementService::createUserTag");
    }
}