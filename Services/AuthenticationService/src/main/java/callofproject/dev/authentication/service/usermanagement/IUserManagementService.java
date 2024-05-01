package callofproject.dev.authentication.service.usermanagement;

import callofproject.dev.authentication.dto.*;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.repository.authentication.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Service class for managing users.
 * It implements the IUserManagementService interface.
 */
public interface IUserManagementService
{
    /**
     * Save User with given dto class.
     *
     * @param userDTO represent the dto class
     * @return UserSaveDTO.
     */
    ResponseMessage<UserSaveDTO> saveUser(UserSignUpRequestDTO userDTO);

    /**
     * Save User with given dto class. Use for testing.
     *
     * @param userDTO represent the dto class
     * @return UserSaveDTO.
     */
    ResponseMessage<UserSaveDTO> saveUserForMobile(UserSignUpRequestDTO userDTO);

    /**
     * Find user with given username
     *
     * @param username represent the username.
     * @return UserDTO class.
     */
    ResponseMessage<UserDTO> findUserByUsername(String username);

    /**
     * Find user with given username
     *
     * @param username represent the username.
     * @return UserResponseDTO class.
     */
    UserResponseDTO<User> findUserByUsernameForAuthenticationService(String username);

    /**
     * Find all users pageable.
     *
     * @param page represent the page number.
     * @param word represent the word.
     * @return UserDTO class.
     */
    MultipleResponseMessagePageable<Object> findAllUsersPageableByContainsWord(int page, String word);

    /**
     * Upsert user profile.
     *
     * @param cv   represent the user cv.
     * @param file represent the user profile.
     * @param dto  represent the dto class.
     * @return UserDTO class.
     */
    ResponseMessage<Object> upsertUserProfile(UserProfileUpdateDTO dto, MultipartFile file, MultipartFile cv);

    /**
     * Find user profile by user id.
     *
     * @param userId represent the user id.
     * @return UserDTO class.
     */
    ResponseMessage<Object> findUserProfileByUserId(UUID userId);

    /**
     * Find user profile by username.
     *
     * @param username represent the username.
     * @return UserDTO class.
     */
    ResponseMessage<Object> findUserProfileByUsername(String username);

    /**
     * Find user with profile by user id.
     *
     * @param userId represent the user id.
     * @return UserDTO class.
     */
    ResponseMessage<Object> findUserWithProfile(UUID userId);

    /**
     * Update about me with given user id and about me.
     *
     * @param userId  represent the user id.
     * @param aboutMe represent the about me.
     * @return MessageResponseDTO.
     */
    ResponseMessage<Object> updateAboutMe(UUID userId, String aboutMe);

    /**
     * Upload user profile photo.
     *
     * @param userId represent the user id.
     * @param file   represent the file.
     * @return ResponseMessage.
     */
    ResponseMessage<Object> uploadUserProfilePhoto(UUID userId, MultipartFile file);

    /**
     * Upload user cv.
     *
     * @param userId represent the user id.
     * @param file   represent the file.
     * @return ResponseMessage.
     */
    ResponseMessage<Object> uploadUserCv(UUID userId, MultipartFile file);


    /**
     * Create User Tag.
     *
     * @param dto represent the dto class
     * @return user tags.
     */
    ResponseMessage<Object> createUserTag(UserTagCreateDTO dto);

    /**
     * Update User Tag.
     *
     * @param dto represent the dto class
     * @return user tags.
     */
    ResponseMessage<Object> updateUserTag(UserTagUpdateDTO dto);

    /**
     * Find User Tags by User Id.
     *
     * @param userId represent the user id.
     * @return user tags.
     */
    ResponseMessage<Object> deleteUserTag(UUID userId, UUID tagId);
}
