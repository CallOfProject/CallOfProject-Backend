package callofproject.dev.authentication.service;


import callofproject.dev.authentication.config.kafka.KafkaProducer;
import callofproject.dev.authentication.dto.*;
import callofproject.dev.authentication.mapper.*;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.nosql.dal.MatchServiceHelper;
import callofproject.dev.repository.authentication.dal.UserManagementServiceHelper;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.repository.authentication.entity.UserProfile;
import callofproject.dev.service.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static callofproject.dev.authentication.util.Util.USER_MANAGEMENT_SERVICE;
import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.repository.authentication.BeanName.USER_MANAGEMENT_DAL_BEAN;
import static java.lang.String.format;
import static java.util.stream.StreamSupport.stream;


@Service(USER_MANAGEMENT_SERVICE)
@Lazy
public class UserManagementService
{
    private final KafkaProducer m_userProducer;
    private final UserManagementServiceHelper m_serviceHelper;
    private final MatchServiceHelper m_matchDbRepository;
    private final IUserMapper m_userMapper;
    private final IEducationMapper m_educationMapper;
    private final ICourseMapper m_courseMapper;
    private final IExperienceMapper m_experienceMapper;
    private final ILinkMapper m_linkMapper;

    public UserManagementService(KafkaProducer userProducer, @Qualifier(USER_MANAGEMENT_DAL_BEAN) UserManagementServiceHelper serviceHelper,
                                 MatchServiceHelper matchDbRepository,
                                 IUserMapper userMapper, IEducationMapper educationMapper, ICourseMapper courseMapper, IExperienceMapper experienceMapper, ILinkMapper linkMapper)
    {
        m_userProducer = userProducer;
        m_serviceHelper = serviceHelper;
        m_matchDbRepository = matchDbRepository;
        m_userMapper = userMapper;
        m_educationMapper = educationMapper;
        m_courseMapper = courseMapper;
        m_experienceMapper = experienceMapper;
        m_linkMapper = linkMapper;
    }

    /**
     * Save User with given dto class.
     *
     * @param userDTO represent the dto class
     * @return UserSaveDTO.
     */
    public UserSaveDTO saveUser(UserSignUpRequestDTO userDTO)
    {
        return doForDataService(() -> saveUserCallback(userDTO), "User cannot be saved!");
    }

    /**
     * Find user with given username
     *
     * @param username represent the username.
     * @return UserDTO class.
     */
    public UserDTO findUserByUsername(String username)
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
    public MultipleMessageResponseDTO<UsersDTO> findAllUsersPageableByContainsWord(int page, String word)
    {
        return doForDataService(() -> findAllUsersPageableByContainsWordCallback(page, word), "UserManagementService::findAllUsersPageableByContainsWord");
    }


    /**
     * Update user profile with given dto class.
     *
     * @param dto represent the dto class
     * @return MessageResponseDTO.
     */
    public MessageResponseDTO<Object> upsertUserProfile(UserProfileUpdateDTO dto)
    {
        return doForDataService(() -> upsertUserProfileCallback(dto), "UserManagementService::upsertUserProfile");
    }


    public MessageResponseDTO<Object> upsertEducation(EducationUpsertDTO dto)
    {
        var upsertedEducation = m_serviceHelper.getEducationServiceHelper().saveEducation(m_educationMapper.toEducation(dto));

        return new MessageResponseDTO<>("Education upserted successfully!", 200, upsertedEducation);
    }

    public MessageResponseDTO<Object> upsertExperience(ExperienceUpsertDTO dto)
    {
        var upsertedExperience = m_serviceHelper.getExperienceServiceHelper().saveExperience(m_experienceMapper.toExperience(dto));

        return new MessageResponseDTO<>("Experience upserted successfully!", 200, upsertedExperience);
    }

    public MessageResponseDTO<Object> upsertCourse(CourseUpsertDTO dto)
    {
        var upsertedCourse = m_serviceHelper.getCourseServiceHelper().saveCourse(m_courseMapper.toCourse(dto));

        return new MessageResponseDTO<>("Course upserted successfully!", 200, upsertedCourse);
    }

    public MessageResponseDTO<Object> upsertLink(LinkUpsertDTO dto)
    {
        var upsertedLink = m_serviceHelper.getLinkServiceHelper().saveLink(m_linkMapper.toLink(dto));

        return new MessageResponseDTO<>("Link upserted successfully!", 200, upsertedLink);
    }

    private MessageResponseDTO<Object> upsertUserProfileCallback(UserProfileUpdateDTO dto)
    {
        var user = m_serviceHelper.getUserServiceHelper().findById(dto.userId());

        if (user.isEmpty())
            throw new DataServiceException("User does not exists!");

        var userProfile = m_serviceHelper.getUserProfileServiceHelper().findUserProfileByUserId(dto.userId());

        if (userProfile.isEmpty())
            throw new DataServiceException("User profile does not exists!");

        userProfile.get().setAboutMe(dto.aboutMe());
        userProfile.get().setCv(dto.cv());
        userProfile.get().setProfilePhoto(dto.profilePhoto());

        var educationList = m_serviceHelper.getEducationServiceHelper().findAllByIds(dto.educationIds());
        var experienceList = m_serviceHelper.getExperienceServiceHelper().findAllByIds(dto.experienceIds());
        var courseList = m_serviceHelper.getCourseServiceHelper().findAllByIds(dto.courseIds());
        var linkList = m_serviceHelper.getLinkServiceHelper().findAllByIds(dto.linkIds());

        if (educationList == null || experienceList == null || courseList == null || linkList == null)
            throw new DataServiceException("Education, experience, course or link does not exists!");

        userProfile.get().clearAllEnvironments();
        userProfile.get().addAllEnvironments(educationList, experienceList, courseList, linkList);

        var savedUserProfile = m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(userProfile.get());

        return new MessageResponseDTO<>("User profile updated successfully!", 200, savedUserProfile);
    }

    //-----------------------------------------------------CALLBACK-----------------------------------------------------

    /**
     * Save User with given dto class.
     *
     * @param userDTOs represent the dto class
     * @return Iterable<User>.
     */
    public Iterable<User> saveUsers(List<UserSignUpRequestDTO> userDTOs) throws DataServiceException
    {
        try
        {
            var list = new ArrayList<User>();
            for (var userDTO : userDTOs)
            {
                var user = m_userMapper.toUser(userDTO);
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

    /**
     * find total page.
     *
     * @return total page.
     */
    private long getTotalPage()
    {
        return m_serviceHelper.getUserServiceHelper().getPageSize();
    }


    /**
     * Save User with given dto class.
     *
     * @param userDTO represent the dto class
     * @return UserSaveDTO.
     */
    public UserSaveDTO saveUserCallback(UserSignUpRequestDTO userDTO)
    {
        var user = m_userMapper.toUser(userDTO);

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

        var kafkaMessage = new UserKafkaDTO(user.getUserId(), user.getUsername(), user.getEmail(), user.getFirstName(),
                user.getMiddleName(), user.getLastName(), Operation.CREATE, 0, 0, 0);

        m_userProducer.sendMessage(kafkaMessage);

        return new UserSaveDTO(token, refreshToken, true, savedUser.getUserId());
    }

    /**
     * Find user with given username
     *
     * @param username represent the username.
     * @return UserDTO class.
     */
    public UserDTO findUserByUsernameCallback(String username)
    {
        var user = m_serviceHelper.getUserServiceHelper().findByUsername(username);

        if (user.isEmpty())
            throw new DataServiceException("User does not exists");

        return m_userMapper.toUserDTO(user.get());
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
    public MultipleMessageResponseDTO<UsersDTO> findAllUsersPageableByContainsWordCallback(int page, String word)
    {
        var dtoList = m_userMapper.toUsersDTO(stream(m_serviceHelper.getUserServiceHelper()
                .findUsersByUsernameContainsIgnoreCase(word, page).spliterator(), true)
                .map(m_userMapper::toUserDTO)
                .toList());

        var msg = format("%d user found!", dtoList.users().size());

        return new MultipleMessageResponseDTO<>(getTotalPage(), page, dtoList.users().size(), msg, dtoList);
    }


}
