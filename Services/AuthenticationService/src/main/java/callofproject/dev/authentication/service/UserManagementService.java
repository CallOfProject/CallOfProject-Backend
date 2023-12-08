package callofproject.dev.authentication.service;


import callofproject.dev.authentication.config.kafka.KafkaProducer;
import callofproject.dev.authentication.dto.*;
import callofproject.dev.authentication.dto.client.CompanySaveDTO;
import callofproject.dev.authentication.dto.client.CourseOrganizationSaveDTO;
import callofproject.dev.authentication.dto.client.CourseSaveDTO;
import callofproject.dev.authentication.dto.client.UniversitySaveDTO;
import callofproject.dev.authentication.dto.environments.CourseUpsertDTO;
import callofproject.dev.authentication.dto.environments.EducationUpsertDTO;
import callofproject.dev.authentication.dto.environments.ExperienceUpsertDTO;
import callofproject.dev.authentication.dto.environments.LinkUpsertDTO;
import callofproject.dev.authentication.dto.user_profile.UserProfileDTO;
import callofproject.dev.authentication.mapper.*;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.nosql.dal.MatchServiceHelper;
import callofproject.dev.repository.authentication.dal.UserManagementServiceHelper;
import callofproject.dev.repository.authentication.entity.*;
import callofproject.dev.service.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.*;

import static callofproject.dev.authentication.util.Util.USER_MANAGEMENT_SERVICE;
import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.repository.authentication.BeanName.USER_MANAGEMENT_DAL_BEAN;
import static callofproject.dev.util.stream.StreamUtil.toListConcurrent;
import static java.lang.String.format;
import static java.util.Optional.of;


@Service(USER_MANAGEMENT_SERVICE)
@Lazy
public class UserManagementService
{
    private final KafkaProducer m_userProducer;
    private final UserManagementServiceHelper m_serviceHelper;
    private final MatchServiceHelper m_matchDbRepository;
    private final IUserMapper m_userMapper;
    private final IUserProfileMapper m_userProfileMapper;
    private final IEducationMapper m_educationMapper;
    private final ICourseMapper m_courseMapper;
    private final IExperienceMapper m_experienceMapper;
    private final ILinkMapper m_linkMapper;
    private final IUserRateMapper m_rateMapper;
    private final IEnvironmentClientService m_environmentClient;

    public UserManagementService(KafkaProducer userProducer, @Qualifier(USER_MANAGEMENT_DAL_BEAN) UserManagementServiceHelper serviceHelper,
                                 MatchServiceHelper matchDbRepository,
                                 IUserMapper userMapper, IUserProfileMapper userProfileMapper, IEducationMapper educationMapper, ICourseMapper courseMapper,
                                 IExperienceMapper experienceMapper, ILinkMapper linkMapper,
                                 IUserRateMapper rateMapper, IEnvironmentClientService environmentClient)
    {
        m_userProducer = userProducer;
        m_serviceHelper = serviceHelper;
        m_matchDbRepository = matchDbRepository;
        m_userMapper = userMapper;
        m_userProfileMapper = userProfileMapper;
        m_educationMapper = educationMapper;
        m_courseMapper = courseMapper;
        m_experienceMapper = experienceMapper;
        m_linkMapper = linkMapper;
        m_rateMapper = rateMapper;
        m_environmentClient = environmentClient;
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

    public ResponseMessage<Object> findUserProfileByUserId(UUID userId)
    {
        return doForDataService(() -> findUserProfileByUserIdCallback(userId), "UserManagementService::findUserProfileByUserId");
    }

    public ResponseMessage<Object> findUserProfileByUsername(String username)
    {
        return doForDataService(() -> findUserProfileByUserUsernameCallback(username), "UserManagementService::findUserProfileByUserId");
    }


    private String convert(String str)
    {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        return str.replaceAll("[^\\p{ASCII}]", "").trim().toUpperCase().replaceAll("\\s+", "_");
    }

    public ResponseMessage<Object> upsertEducation(EducationUpsertDTO dto)
    {

        // save to environment client if not exists
        var educationOnClient = m_environmentClient.saveUniversity(new UniversitySaveDTO(dto.getSchoolName()));

        // find user profile
        var userProfile = m_serviceHelper.getUserProfileServiceHelper().findUserProfileByUserId(dto.getUserId());

        // if user profile not exists, throw exception
        if (userProfile.isEmpty())
            throw new DataServiceException("User does not exists!");

        // check if course exists
        var isExistsEducation = userProfile.get().getEducationList().stream()
                .anyMatch(c ->
                        c.getSchoolName().equals(educationOnClient.getUniversityName()) &&
                                convert(c.getDepartment()).equals(convert(dto.getDepartment())));

        if (isExistsEducation)
            return new ResponseMessage<>("Education already exists!", 400, null);

        // save course organization
        var education = new Education(educationOnClient.getId(), educationOnClient.getUniversityName(), dto.getDepartment(),
                dto.getDescription(), dto.getStartDate(), dto.getFinishDate(), dto.isContinue(), dto.getGpa());

        var savedEducation = m_serviceHelper.getEducationServiceHelper().saveEducation(education);

        userProfile.get().addEducation(savedEducation);
        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(userProfile.get());

        return new ResponseMessage<>("Education upserted successfully!", 200, savedEducation);
    }

    public ResponseMessage<Object> upsertExperience(ExperienceUpsertDTO dto)
    {
        // save to environment client if not exists
        var experienceOnClient = m_environmentClient.saveCompany(new CompanySaveDTO(dto.getCompanyName()));

        // find user profile
        var userProfile = m_serviceHelper.getUserProfileServiceHelper().findUserProfileByUserId(dto.getUserId());

        // if user profile not exists, throw exception
        if (userProfile.isEmpty())
            throw new DataServiceException("User does not exists!");

        // check if course exists
        var isExistsExperience = userProfile.get().getExperienceList().stream()
                .anyMatch(c -> c.getCompanyName().equals(experienceOnClient.getCompanyName()) &&
                        c.getJobDefinition().equals(dto.getJobDefinition()));

        if (isExistsExperience)
            return new ResponseMessage<>("Experience already exists!", 400, null);

        // save course organization
        var experience = new Experience(experienceOnClient.getId(), experienceOnClient.getCompanyName(), dto.getDescription(),
                dto.getCompanyWebsite(), dto.getStartDate(), dto.getFinishDate(), dto.isContinue(), dto.getJobDefinition());

        var savedExperience = m_serviceHelper.getExperienceServiceHelper().saveExperience(experience);

        userProfile.get().addExperience(savedExperience);
        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(userProfile.get());

        return new ResponseMessage<>("Course upserted successfully!", 200, savedExperience);
    }

    public ResponseMessage<Object> upsertCourse(CourseUpsertDTO dto)
    {
        // save to environment client if not exists
        var courseOnClient = m_environmentClient.saveCourse(new CourseSaveDTO(dto.getCourseName()));

        // save course organization not exists
        var organizationOnClient = m_environmentClient.saveCourseOrganization(new CourseOrganizationSaveDTO(dto.getOrganizator()));

        // find user profile
        var userProfile = m_serviceHelper.getUserProfileServiceHelper().findUserProfileByUserId(dto.getUserId());

        // if user profile not exists, throw exception
        if (userProfile.isEmpty())
            throw new DataServiceException("User does not exists!");

        // check if course exists
        var isExistsCourse = userProfile.get().getCourseList().stream()
                .anyMatch(c -> c.getCourseName().equals(courseOnClient.getCourseName()) && c.getCourseOrganization()
                        .getCourseOrganizationName().equals(organizationOnClient.getCourseOrganizationName()));

        if (isExistsCourse)
            return new ResponseMessage<>("Course already exists!", 400, null);

        // check course organization exists
        var existingOrganization = m_serviceHelper.getCourseOrganizationServiceHelper()
                .findByCourseOrganizationNameContainsIgnoreCase(organizationOnClient.getCourseOrganizationName());

        // if not exists, save course organization
        if (existingOrganization.isEmpty())
            existingOrganization = of(m_serviceHelper.getCourseOrganizationServiceHelper()
                    .saveCourseOrganization(new CourseOrganization(organizationOnClient.getCourseOrganizationName())));

        // save course organization
        var course = new Course(courseOnClient.getId(), courseOnClient.getCourseName(), dto.getStartDate(), dto.getFinishDate(), dto.isContinue(),
                dto.getDescription(), existingOrganization.get());

        var savedCourse = m_serviceHelper.getCourseServiceHelper().saveCourse(course);

        userProfile.get().addCourse(course);
        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(userProfile.get());

        return new ResponseMessage<>("Course upserted successfully!", 200, savedCourse);
    }

    public ResponseMessage<Object> upsertLink(LinkUpsertDTO dto)
    {
        var upsertedLink = doForDataService(() -> m_serviceHelper.getLinkServiceHelper().saveLink(m_linkMapper.toLink(dto)), "Link cannot be upserted!");

        return new ResponseMessage<>("Link upserted successfully!", 200, upsertedLink);
    }

    private ResponseMessage<Object> upsertUserProfileCallback(UserProfileUpdateDTO dto)
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

        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(userProfile.get());

        return new ResponseMessage<>("User profile updated successfully!", 200, getUserProfile(user.get()));
    }

    //-----------------------------------------------------CALLBACK-----------------------------------------------------

    private ResponseMessage<Object> findUserProfileByUserUsernameCallback(String username)
    {
        var user = m_serviceHelper.getUserServiceHelper().findByUsername(username);

        if (user.isEmpty())
            throw new DataServiceException("User does not exists!");

        var profileMap = getUserProfile(user.get());

        return new ResponseMessage<>("User profile found!", 200, profileMap);
    }

    private UserProfileDTO getUserProfile(User user)
    {
        var userProfile = user.getUserProfile();

        var educations = m_educationMapper.toEducationsDTO(toListConcurrent(userProfile.getEducationList(),
                m_educationMapper::toEducationDTO));

        var experiences = m_experienceMapper.toExperiencesDTO(toListConcurrent(userProfile.getExperienceList(), m_experienceMapper::toExperienceDTO));

        var courses = m_courseMapper.toCoursesDTO(toListConcurrent(userProfile.getCourseList(), m_courseMapper::toCourseDTO));

        var links = m_linkMapper.toLinksDTO(toListConcurrent(userProfile.getLinkList(), m_linkMapper::toLinkDTO));

        var userRate = m_rateMapper.toUserRateDTO(userProfile.getUserRate());

        return m_userProfileMapper.toUserProfileDTO(userProfile, educations, experiences, courses, links, userRate);
    }

    private ResponseMessage<Object> findUserProfileByUserIdCallback(UUID userId)
    {
        var user = m_serviceHelper.getUserServiceHelper().findById(userId);

        if (user.isEmpty())
            throw new DataServiceException("User does not exists!");

        var profileMap = getUserProfile(user.get());

        return new ResponseMessage<>("User profile found!", 200, profileMap);
    }

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
     * Save User with given dto class.
     *
     * @param userDTO represent the dto class
     * @return UserSaveDTO.
     */
    public UserSaveDTO saveUserCallback(UserSignUpRequestDTO userDTO)
    {
        var user = m_userMapper.toUser(userDTO);
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
    public ResponseMessage<UserDTO> findUserByUsernameCallback(String username)
    {
        var user = doForDataService(() -> m_serviceHelper.getUserServiceHelper().findByUsername(username),
                "User does not exists!");

        return user.map(value -> new ResponseMessage<>("User found!", 200, m_userMapper.toUserDTO(value)))
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

        var dtoList = m_userMapper.toUsersDTO(toListConcurrent(userListPageable.getContent(), m_userMapper::toUserDTO));

        var msg = format("%d user found!", dtoList.users().size());

        return new MultipleResponseMessagePageable<>(userListPageable.getTotalPages(), page, dtoList.users().size(), msg, dtoList);
    }


    public boolean removeEducation(UUID userId, UUID id)
    {

        var user = m_serviceHelper.getUserServiceHelper().findById(userId);

        if (user.isEmpty())
            throw new DataServiceException("User does not exists!");

        var userProfile = user.get().getUserProfile();

        var education = userProfile.getEducationList().stream().filter(e -> e.getEducation_id().equals(id)).findFirst();

        if (education.isEmpty())
            throw new DataServiceException("Education does not exists!");

        userProfile.getEducationList().remove(education.get());

        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(userProfile);
        m_serviceHelper.getEducationServiceHelper().removeEducation(education.get());

        return true;
    }
}
