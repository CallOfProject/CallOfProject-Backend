package callofproject.dev.authentication.service;


import callofproject.dev.authentication.config.kafka.KafkaProducer;
import callofproject.dev.authentication.dto.*;
import callofproject.dev.authentication.dto.user_profile.UserProfileDTO;
import callofproject.dev.authentication.dto.user_profile.UserWithProfileDTO;
import callofproject.dev.authentication.mapper.MapperConfiguration;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.repository.authentication.dal.UserManagementServiceHelper;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.repository.authentication.entity.UserProfile;
import callofproject.dev.repository.authentication.enumeration.RoleEnum;
import callofproject.dev.service.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

import static callofproject.dev.authentication.util.Util.MAPPER_CONFIG_BEAN;
import static callofproject.dev.authentication.util.Util.USER_MANAGEMENT_SERVICE;
import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.repository.authentication.BeanName.USER_MANAGEMENT_DAL_BEAN;
import static callofproject.dev.util.stream.StreamUtil.toListConcurrent;
import static java.lang.String.format;

/**
 * Service class for managing users.
 * It implements the IUserManagementService interface.
 */
@Service(USER_MANAGEMENT_SERVICE)
@Lazy
public class UserManagementService
{
    private final KafkaProducer m_userProducer;
    private final UserManagementServiceHelper m_serviceHelper;
    private final MapperConfiguration m_mapperConfig;
    private final S3Service m_storageService;

    /**
     * Constructor for the UserManagementService class.
     * It is used to inject dependencies into the service.
     *
     * @param serviceHelper The UserManagementServiceHelper object to be injected.
     * @param mapperConfig  The MapperConfiguration object to be injected.
     * @param userProducer  The KafkaProducer object to be injected.
     */
    public UserManagementService(@Qualifier(USER_MANAGEMENT_DAL_BEAN) UserManagementServiceHelper serviceHelper,
                                 @Qualifier(MAPPER_CONFIG_BEAN) MapperConfiguration mapperConfig,
                                 KafkaProducer userProducer, S3Service storageService)
    {
        m_userProducer = userProducer;
        m_serviceHelper = serviceHelper;
        m_mapperConfig = mapperConfig;
        m_storageService = storageService;
    }

    /**
     * Save User with given dto class.
     *
     * @param userDTO represent the dto class
     * @return UserSaveDTO.
     */
    public ResponseMessage<UserSaveDTO> saveUser(UserSignUpRequestDTO userDTO)
    {
        var result = doForDataService(() -> saveUserCallback(userDTO), "User cannot be saved!");

        if (result.getStatusCode() == 200)
            PublishUser(result.getObject().userId());

        return result;
    }


    /**
     * publish user to kafka
     *
     * @param uuid represent the uuid
     */
    private void PublishUser(UUID uuid)
    {
        var user = getUserIfExists(uuid);

        var kafkaMessage = new UserKafkaDTO(user.getUserId(), user.getUsername(), user.getEmail(), user.getFirstName(),
                user.getMiddleName(), user.getLastName(), EOperation.CREATE, user.getPassword(), user.getRoles(),
                user.getDeleteAt(), 0, 0, 0);

        m_userProducer.sendMessage(kafkaMessage);
    }

    /**
     * Find user with given username
     *
     * @param username represent the username.
     * @return UserDTO class.
     */
    public ResponseMessage<UserDTO> findUserByUsername(String username)
    {
        return doForDataService(() -> findUserByUsernameCallback(username), "User does not exists!");
    }

    /**
     * Find User with given username but returns the user entity.
     *
     * @param username represent the username.
     * @return User class.
     */
    public UserResponseDTO<User> findUserByUsernameForAuthenticationService(String username)
    {
        return doForDataService(() -> findUserByUsernameForAuthenticationServiceCallback(username), "User does not exists!");
    }

    /**
     * Find all users with given word and page.
     *
     * @param page represent the page.
     * @param word represent the containing word.
     * @return UsersDTO class.
     */
    public MultipleResponseMessagePageable<Object> findAllUsersPageableByContainsWord(int page, String word)
    {
        return doForDataService(() -> findAllUsersPageableByContainsWordCallback(page, word), "UserManagementService::findAllUsersPageableByContainsWord");
    }


    /**
     * Update user profile with given dto class.
     *
     * @param dto represent the dto class
     * @return MessageResponseDTO.
     */
    public ResponseMessage<Object> upsertUserProfile(UserProfileUpdateDTO dto)
    {
        return doForDataService(() -> upsertUserProfileCallback(dto), "UserManagementService::upsertUserProfile");
    }

    /**
     * Find user profile with given user id.
     *
     * @param userId represent the user id.
     * @return UserProfileDTO class.
     */
    public ResponseMessage<Object> findUserProfileByUserId(UUID userId)
    {
        return doForDataService(() -> findUserProfileByUserIdCallback(userId), "UserManagementService::findUserProfileByUserId");
    }

    /**
     * Find user profile with given username.
     *
     * @param username represent the username.
     * @return UserProfileDTO class.
     */
    public ResponseMessage<Object> findUserProfileByUsername(String username)
    {
        return doForDataService(() -> findUserProfileByUserUsernameCallback(username), "UserManagementService::findUserProfileByUserId");
    }

    /**
     * Find user and his/her profile with given id.
     *
     * @param userId represent the user id.
     * @return UserWithProfileDTO class.
     */
    public ResponseMessage<Object> findUserWithProfile(UUID userId)
    {
        return doForDataService(() -> findUserWithProfileCallback(userId), "UserManagementService::findUserWithProfile");
    }


    //------------------------------------------------------------------------------------------------------------------
    //####################################################-CALLBACKS-###################################################
    //------------------------------------------------------------------------------------------------------------------


    /**
     * Save User with given dto class.
     *
     * @param userDTO represent the dto class
     * @return UserSaveDTO.
     */
    public ResponseMessage<UserSaveDTO> saveUserCallback(UserSignUpRequestDTO userDTO)
    {
        var user = m_mapperConfig.userMapper.toUser(userDTO);
        user.setAccountBlocked(true);

        var userProfile = new UserProfile();

        userProfile.setUser(user);
        user.setUserProfile(userProfile);

        var savedUser = m_serviceHelper.getUserServiceHelper().saveUser(user);

        if (savedUser == null)
            throw new DataServiceException("User cannot be saved!");

        var claims = new HashMap<String, Object>();
        var authorities = JwtUtil.populateAuthorities(user.getRoles());
        claims.put("authorities", authorities);

        var token = JwtUtil.generateToken(claims, user.getUsername());
        var refreshToken = JwtUtil.generateToken(claims, user.getUsername());

        return new ResponseMessage<>("User saved successfully!", 200, new UserSaveDTO(token, refreshToken, true, savedUser.getUserId()));
    }


    /**
     * Find user with given username
     *
     * @param username represent the username.
     * @return UserDTO class.
     */
    public ResponseMessage<UserDTO> findUserByUsernameCallback(String username)
    {
        var user = doForDataService(() -> m_serviceHelper.getUserServiceHelper().findByUsername(username),
                "User does not exists!");

        return user.map(value -> new ResponseMessage<>("User found!", 200, m_mapperConfig.userMapper.toUserDTO(value)))
                .orElseGet(() -> new ResponseMessage<>("User does not exists!", 400, null));

    }


    /**
     * Find User with given username but returns the user entity.
     *
     * @param username represent the username.
     * @return User class.
     */
    public UserResponseDTO<User> findUserByUsernameForAuthenticationServiceCallback(String username)
    {
        var user = m_serviceHelper.getUserServiceHelper().findByUsername(username);

        if (user.isEmpty())
            throw new DataServiceException("User does not exists");

        return new UserResponseDTO<User>(true, user.get());
    }


    /**
     * Find all users with given word and page.
     *
     * @param page represent the page.
     * @param word represent the containing word.
     * @return UsersDTO class.
     */
    public MultipleResponseMessagePageable<Object> findAllUsersPageableByContainsWordCallback(int page, String word)
    {
        var userListPageable = m_serviceHelper.getUserServiceHelper().findUsersByUsernameContainsIgnoreCase(word, page);

        var dtoList = m_mapperConfig.userMapper.toUsersDTO(toListConcurrent(userListPageable.getContent(), m_mapperConfig.userMapper::toUserDTO));

        var msg = format("%d user found!", dtoList.users().size());

        return new MultipleResponseMessagePageable<>(userListPageable.getTotalPages(), page, dtoList.users().size(), msg, dtoList);
    }


    /**
     * Update user profile with given dto class.
     *
     * @param user represent the entity class
     * @return MessageResponseDTO.
     */
    private String findTopRole(User user)
    {
        var role = RoleEnum.ROLE_USER.getRole();

        for (var r : user.getRoles())
        {
            if (r.getName().equals(RoleEnum.ROLE_ROOT.getRole()))
            {
                role = RoleEnum.ROLE_ROOT.getRole();
                break;
            }
            if (r.getName().equals(RoleEnum.ROLE_ADMIN.getRole()))
                role = RoleEnum.ROLE_ADMIN.getRole();
        }
        return role;
    }


    /**
     * Update user profile with given dto class.
     *
     * @param dto represent the dto class
     * @return MessageResponseDTO.
     */
    private ResponseMessage<Object> upsertUserProfileCallback(UserProfileUpdateDTO dto)
    {
        var user = getUserIfExists(dto.userId());

        var userProfile = m_serviceHelper.getUserProfileServiceHelper().findUserProfileByUserId(dto.userId());

        if (userProfile.isEmpty())
            throw new DataServiceException("User profile does not exists!");

        userProfile.get().setAboutMe(dto.aboutMe());
        userProfile.get().setCv(dto.cv());
        userProfile.get().setProfilePhoto(dto.profilePhoto());

        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(userProfile.get());

        return new ResponseMessage<>("User profile updated successfully!", 200, getUserProfile(user));
    }


    /**
     * Find user profile with given user id.
     *
     * @param userId represent the user id.
     * @return UserProfileDTO class.
     */
    private ResponseMessage<Object> findUserProfileByUserIdCallback(UUID userId)
    {
        var profileMap = getUserProfile(getUserIfExists(userId));

        return new ResponseMessage<>("User profile found!", 200, profileMap);
    }


    /**
     * Find user profile with given username.
     *
     * @param username represent the username.
     * @return UserProfileDTO class.
     */
    private ResponseMessage<Object> findUserProfileByUserUsernameCallback(String username)
    {
        var user = m_serviceHelper.getUserServiceHelper().findByUsername(username);

        if (user.isEmpty())
            throw new DataServiceException("User does not exists!");

        var profileMap = getUserProfile(user.get());

        return new ResponseMessage<>("User profile found!", 200, profileMap);
    }


    /**
     * Find user and his/her profile with given id.
     *
     * @param uuid represent the user id.
     * @return UserWithProfileDTO class.
     */
    private ResponseMessage<Object> findUserWithProfileCallback(UUID uuid)
    {
        var user = getUserIfExists(uuid);
        var userProfile = getUserProfile(user);
        var userWithProfile = new UserWithProfileDTO(user.getUserId(), m_mapperConfig.userMapper.toUserDTO(user), userProfile);

        return new ResponseMessage<>("User with profile found!", 200, userWithProfile);
    }

    //------------------------------------------------------------------------------------------------------------------
    //##################################################-HELPER METHODS-################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get user profile with given user.
     *
     * @param user represent the user.
     * @return UserProfileDTO class.
     */
    private UserProfileDTO getUserProfile(User user)
    {
        var userProfile = user.getUserProfile();

        var educations = m_mapperConfig.educationMapper.toEducationsDTO(toListConcurrent(userProfile.getEducationList(), m_mapperConfig.educationMapper::toEducationDTO));

        var experiences = m_mapperConfig.experienceMapper.toExperiencesDTO(toListConcurrent(userProfile.getExperienceList(), m_mapperConfig.experienceMapper::toExperienceDTO));

        var courses = m_mapperConfig.courseMapper.toCoursesDTO(toListConcurrent(userProfile.getCourseList(), m_mapperConfig.courseMapper::toCourseDTO));

        var links = m_mapperConfig.linkMapper.toLinksDTO(toListConcurrent(userProfile.getLinkList(), m_mapperConfig.linkMapper::toLinkDTO));

        var userRate = m_mapperConfig.userRateMapper.toUserRateDTO(userProfile.getUserRate());

        return m_mapperConfig.userProfileMapper.toUserProfileDTO(userProfile, educations, experiences, courses, links, userRate);
    }


    /**
     * Get user with given user id.
     *
     * @param userId represent the user id.
     * @return User class.
     */
    public User getUserIfExists(UUID userId)
    {
        var user = m_serviceHelper.getUserServiceHelper().findById(userId);

        if (user.isEmpty())
            throw new DataServiceException("User does not exists!");

        return user.get();
    }


    /**
     * Save Users with given dto class.
     *
     * @param userDTOs represent the dto class
     * @return UserSaveDTO.
     */
    public Iterable<User> saveUsers(List<UserSignUpRequestDTO> userDTOs) throws DataServiceException
    {
        try
        {
            var list = new ArrayList<User>();

            for (var userDTO : userDTOs)
            {
                var user = m_mapperConfig.userMapper.toUser(userDTO);
                var userProfile = new UserProfile();

                userProfile.setUser(user);
                user.setUserProfile(userProfile);
                list.add(user);
            }

            return m_serviceHelper.getUserServiceHelper().saveAll(list);
        } catch (Exception exception)
        {
            return Collections.emptyList();
        }
    }
}