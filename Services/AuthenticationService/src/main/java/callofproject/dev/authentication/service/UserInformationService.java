package callofproject.dev.authentication.service;

import callofproject.dev.authentication.dto.client.CompanySaveDTO;
import callofproject.dev.authentication.dto.client.CourseOrganizationSaveDTO;
import callofproject.dev.authentication.dto.client.CourseSaveDTO;
import callofproject.dev.authentication.dto.client.UniversitySaveDTO;
import callofproject.dev.authentication.dto.environments.CourseUpsertDTO;
import callofproject.dev.authentication.dto.environments.EducationUpsertDTO;
import callofproject.dev.authentication.dto.environments.ExperienceUpsertDTO;
import callofproject.dev.authentication.dto.environments.LinkUpsertDTO;
import callofproject.dev.authentication.mapper.MapperConfiguration;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.nosql.dal.MatchServiceHelper;
import callofproject.dev.repository.authentication.dal.UserManagementServiceHelper;
import callofproject.dev.repository.authentication.entity.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static callofproject.dev.data.common.util.UtilityMethod.convert;
import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static java.util.Optional.of;

@Service
@Lazy
public class UserInformationService
{
    private final UserManagementServiceHelper m_serviceHelper;
    private final MatchServiceHelper m_matchServiceHelper;
    private final IEnvironmentClientService m_environmentClient;
    private final MapperConfiguration m_mapperConfig;

    public UserInformationService(UserManagementServiceHelper serviceHelper, MatchServiceHelper matchServiceHelper,
                                  IEnvironmentClientService environmentClient, MapperConfiguration mapperConfig)
    {
        m_serviceHelper = serviceHelper;
        m_matchServiceHelper = matchServiceHelper;
        m_environmentClient = environmentClient;
        m_mapperConfig = mapperConfig;
    }

    /**
     * Upsert education with given dto class.
     *
     * @param dto represent the dto class
     * @return MessageResponseDTO.
     */
    public ResponseMessage<Object> upsertEducation(EducationUpsertDTO dto)
    {
        return doForDataService(() -> upsertEducationCallback(dto), "Education cannot be upserted!");
    }

    /**
     * Upsert experience with given dto class.
     *
     * @param dto represent the dto class
     * @return MessageResponseDTO.
     */
    public ResponseMessage<Object> upsertExperience(ExperienceUpsertDTO dto)
    {
        return doForDataService(() -> upsertExperienceCallback(dto), "Experience cannot be upserted!");
    }

    /**
     * Upsert course with given dto class.
     *
     * @param dto represent the dto class
     * @return MessageResponseDTO.
     */
    public ResponseMessage<Object> upsertCourse(CourseUpsertDTO dto)
    {
        return doForDataService(() -> upsertCourseCallback(dto), "Course cannot be upserted!");
    }

    /**
     * Upsert link with given dto class.
     *
     * @param dto represent the dto class
     * @return MessageResponseDTO.
     */
    public ResponseMessage<Object> upsertLink(LinkUpsertDTO dto)
    {
        return doForDataService(() -> upsertLinkCallback(dto), "Link cannot be upserted!");
    }

    /**
     * Remove education with given id.
     *
     * @param userId represent the user id.
     * @param id     represent the education id.
     * @return MessageResponseDTO.
     */
    public ResponseMessage<Object> removeEducation(UUID userId, UUID id)
    {
        return doForDataService(() -> removeEducationCallback(userId, id), "Education cannot be removed!");
    }

    /**
     * Remove course with given id.
     *
     * @param userId represent the user id.
     * @param id     represent the course id.
     * @return MessageResponseDTO.
     */
    public ResponseMessage<Object> removeCourse(UUID userId, UUID id)
    {
        return doForDataService(() -> removeCourseCallback(userId, id), "Course cannot be removed!");
    }

    /**
     * Remove experience with given id.
     *
     * @param userId represent the user id.
     * @param id     represent the experience id.
     * @return MessageResponseDTO.
     */
    public ResponseMessage<Object> removeExperience(UUID userId, UUID id)
    {
        return doForDataService(() -> removeExperienceCallback(userId, id), "Experience cannot be removed!");
    }

    /**
     * Remove link with given id.
     *
     * @param userId represent the user id.
     * @param id     represent the link id.
     * @return MessageResponseDTO.
     */
    public ResponseMessage<Object> removeLink(UUID userId, long id)
    {
        return doForDataService(() -> removeLinkCallback(userId, id), "Link cannot be removed!");
    }

    /**
     * Remove course organization with given id.
     *
     * @param userId represent the user id.
     * @param id     represent the course organization id.
     * @return MessageResponseDTO.
     */
    public ResponseMessage<Object> removeCourseOrganization(UUID userId, UUID id)
    {
        return doForDataService(() -> removeCourseOrganizationCallback(userId, id), "Course organization cannot be removed!");
    }

    //-----------------------------------------------------CALLBACK-----------------------------------------------------
    private UserProfile getUserProfile(UUID userId)
    {
        var userProfile = m_serviceHelper.getUserProfileServiceHelper().findUserProfileByUserId(userId);

        if (userProfile.isEmpty())
            throw new DataServiceException("User profile does not exists!");

        return userProfile.get();
    }

    private ResponseMessage<Object> upsertEducationCallback(EducationUpsertDTO dto)
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

    private ResponseMessage<Object> upsertExperienceCallback(ExperienceUpsertDTO dto)
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

    private ResponseMessage<Object> upsertCourseCallback(CourseUpsertDTO dto)
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

    private ResponseMessage<Object> upsertLinkCallback(LinkUpsertDTO dto)
    {
        var user = getUserProfile(dto.userId());

        var upsertedLink = doForDataService(() -> m_serviceHelper.getLinkServiceHelper()
                .saveLink(m_mapperConfig.linkMapper.toLink(dto)), "Link cannot be upserted!");

        user.addLink(upsertedLink);
        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(user);

        return new ResponseMessage<>("Link upserted successfully!", 200, upsertedLink);
    }

    private ResponseMessage<Object> removeEducationCallback(UUID userId, UUID id)
    {
        var userProfile = getUserProfile(userId);

        var education = userProfile.getEducationList()
                .stream()
                .filter(e -> e.getEducation_id().equals(id))
                .findFirst();

        if (education.isEmpty())
            throw new DataServiceException("Education does not exists!");

        userProfile.getEducationList().remove(education.get());

        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(userProfile);
        m_serviceHelper.getEducationServiceHelper().removeEducation(education.get());

        return new ResponseMessage<>("Education removed successfully!", 200, true);
    }

    private ResponseMessage<Object> removeCourseCallback(UUID userId, UUID id)
    {
        var userProfile = getUserProfile(userId);

        var course = userProfile.getCourseList()
                .stream()
                .filter(e -> e.getCourse_id().equals(id))
                .findFirst();

        if (course.isEmpty())
            throw new DataServiceException("Course does not exists!");

        userProfile.getCourseList().remove(course.get());

        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(userProfile);
        m_serviceHelper.getCourseServiceHelper().removeCourse(course.get());

        return new ResponseMessage<>("Course removed successfully!", 200, true);
    }

    private ResponseMessage<Object> removeExperienceCallback(UUID userId, UUID id)
    {
        var userProfile = getUserProfile(userId);

        var experience = userProfile.getExperienceList()
                .stream()
                .filter(e -> e.getExperience_id().equals(id))
                .findFirst();

        if (experience.isEmpty())
            throw new DataServiceException("Experience does not exists!");

        userProfile.getExperienceList().remove(experience.get());

        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(userProfile);
        m_serviceHelper.getExperienceServiceHelper().removeExperience(experience.get());

        return new ResponseMessage<>("Experience removed successfully!", 200, true);
    }

    private ResponseMessage<Object> removeLinkCallback(UUID userId, long id)
    {
        var userProfile = getUserProfile(userId);

        var link = userProfile.getLinkList()
                .stream()
                .filter(e -> e.getLink_id() == id)
                .findFirst();

        if (link.isEmpty())
            throw new DataServiceException("Link does not exists!");

        userProfile.getLinkList().remove(link.get());

        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(userProfile);
        m_serviceHelper.getLinkServiceHelper().removeLink(link.get());

        return new ResponseMessage<>("Link removed successfully!", 200, true);
    }

    private ResponseMessage<Object> removeCourseOrganizationCallback(UUID userId, UUID id)
    {
        var userProfile = getUserProfile(userId);

        var courseOrganization = userProfile.getCourseList()
                .stream()
                .filter(e -> e.getCourseOrganization().getCourseOrganizationId().equals(id))
                .findFirst();

        if (courseOrganization.isEmpty())
            throw new DataServiceException("Course organization does not exists!");

        userProfile.getCourseList().remove(courseOrganization.get());

        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(userProfile);
        m_serviceHelper.getCourseOrganizationServiceHelper().removeCourseOrganization(courseOrganization.get().getCourseOrganization());

        return new ResponseMessage<>("Course organization removed successfully!", 200, true);
    }
}
