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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.data.common.util.UtilityMethod.convert;
import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static java.util.Optional.of;

/**
 * Service class for managing user information, including education, experience, courses, links, and more.
 */
@Service
@Lazy
public class UserInformationService
{
    private final UserManagementServiceHelper m_serviceHelper;
    private final MatchServiceHelper m_matchServiceHelper;
    private final IEnvironmentClientService m_environmentClient;
    private final MapperConfiguration m_mapperConfig;
    private final S3Service m_s3Service;
    @Value("${application.cv-bucket.name}")
    private String m_cvBucketName;

    /**
     * Constructs a new UserInformationService with the given dependencies.
     *
     * @param serviceHelper      The UserManagementServiceHelper to be used by this service.
     * @param matchServiceHelper The MatchServiceHelper to be used by this service.
     * @param environmentClient  The IEnvironmentClientService to interact with the environment.
     * @param mapperConfig       The MapperConfiguration for mapping DTOs to entities.
     */
    public UserInformationService(UserManagementServiceHelper serviceHelper, MatchServiceHelper matchServiceHelper,
                                  IEnvironmentClientService environmentClient, MapperConfiguration mapperConfig, S3Service s3Service)
    {
        m_serviceHelper = serviceHelper;
        m_matchServiceHelper = matchServiceHelper;
        m_environmentClient = environmentClient;
        m_mapperConfig = mapperConfig;
        m_s3Service = s3Service;
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


    /**
     * Uploads a user profile photo for the given user ID.
     *
     * @param userId The UUID of the user whose profile photo is being uploaded.
     * @param file   The MultipartFile containing the user profile photo.
     * @return A ResponseMessage indicating the success of the profile photo upload operation.
     */
    public ResponseMessage<Object> uploadUserProfilePhoto(UUID userId, MultipartFile file)
    {
        var userProfile = getUserProfile(userId);

        var fileNameSplit = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        var extension = fileNameSplit[fileNameSplit.length - 1];
        var fileName = "cv_" + userProfile.getUser().getUserId() + "_" + userProfile.getUserProfileId() + "." + extension;

        var profilePhoto = m_s3Service.uploadToS3WithMultiPartFileV2(file, fileName, Optional.empty());

        userProfile.setProfilePhoto(profilePhoto);

        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(userProfile);

        return new ResponseMessage<>("Profile photo uploaded successfully!", 200, profilePhoto);
    }

    public ResponseMessage<Object> uploadCV(UUID userId, MultipartFile file)
    {
        var userProfile = getUserProfile(userId);
        var fileNameSplit = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        var extension = fileNameSplit[fileNameSplit.length - 1];
        var fileName = "cv_" + userProfile.getUser().getUserId() + "." + extension;
        System.out.println("FN: " + fileName);
        System.out.println("BN: " + m_cvBucketName);
        var cv = m_s3Service.uploadToS3WithMultiPartFileV2(file, fileName, Optional.of(m_cvBucketName));
        userProfile.setCv(cv);
        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(userProfile);
        return new ResponseMessage<>("CV uploaded successfully!", 200, cv);
    }
    //------------------------------------------------------------------------------------------------------------------
    //####################################################-CALLBACKS-###################################################
    //------------------------------------------------------------------------------------------------------------------


    /**
     * Retrieves the user profile for the given user ID.
     *
     * @param userId The UUID of the user whose profile is being retrieved.
     * @return The UserProfile associated with the given user ID.
     * @throws DataServiceException if the user profile does not exist.
     */
    private UserProfile getUserProfile(UUID userId)
    {
        var userProfile = m_serviceHelper.getUserProfileServiceHelper().findUserProfileByUserId(userId);

        if (userProfile.isEmpty())
            throw new DataServiceException("User profile does not exists!");

        return userProfile.get();
    }

    /**
     * Adds or updates educational information for a user based on the provided EducationUpsertDTO.
     *
     * @param dto The DTO containing education information to be upserted.
     * @return A ResponseMessage indicating the success of the education upsert operation.
     * @throws DataServiceException if the user or education information does not exist.
     */
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

    /**
     * Adds or updates professional experience for a user based on the provided ExperienceUpsertDTO.
     *
     * @param dto The DTO containing experience information to be upserted.
     * @return A ResponseMessage indicating the success of the experience upsert operation.
     * @throws DataServiceException if the user or experience information does not exist.
     */
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

        return new ResponseMessage<>("Experience upserted successfully!", 200, savedExperience);
    }

    /**
     * Adds or updates course information for a user based on the provided CourseUpsertDTO.
     *
     * @param dto The DTO containing course information to be upserted.
     * @return A ResponseMessage indicating the success of the course upsert operation.
     * @throws DataServiceException if the user or course information does not exist.
     */
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

    /**
     * Adds or updates a link for a user based on the provided LinkUpsertDTO.
     *
     * @param dto The DTO containing link information to be upserted.
     * @return A ResponseMessage indicating the success of the link upsert operation.
     */
    private ResponseMessage<Object> upsertLinkCallback(LinkUpsertDTO dto)
    {
        var user = getUserProfile(dto.userId());

        var upsertedLink = doForDataService(() -> m_serviceHelper.getLinkServiceHelper()
                .saveLink(m_mapperConfig.linkMapper.toLink(dto)), "Link cannot be upserted!");

        user.addLink(upsertedLink);
        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(user);

        return new ResponseMessage<>("Link upserted successfully!", 200, upsertedLink);
    }

    /**
     * Removes an education entry for a user based on provided identifiers.
     *
     * @param userId The UUID of the user.
     * @param id     The UUID of the education entry to be removed.
     * @return A ResponseMessage indicating the success of the education removal operation.
     * @throws DataServiceException if the education entry does not exist.
     */
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

    /**
     * Removes a course entry for a user based on provided identifiers.
     *
     * @param userId The UUID of the user.
     * @param id     The UUID of the course entry to be removed.
     * @return A ResponseMessage indicating the success of the course removal operation.
     * @throws DataServiceException if the course entry does not exist.
     */
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

    /**
     * Removes an experience entry for a user based on provided identifiers.
     *
     * @param userId The UUID of the user.
     * @param id     The UUID of the experience entry to be removed.
     * @return A ResponseMessage indicating the success of the experience removal operation.
     * @throws DataServiceException if the experience entry does not exist.
     */
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

    /**
     * Removes a link for a user based on provided identifiers.
     *
     * @param userId The UUID of the user.
     * @param id     The unique ID of the link to be removed.
     * @return A ResponseMessage indicating the success of the link removal operation.
     * @throws DataServiceException if the link does not exist.
     */
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

    /**
     * Removes a course organization entry for a user based on provided identifiers.
     *
     * @param userId The UUID of the user.
     * @param id     The UUID of the course organization entry to be removed.
     * @return A ResponseMessage indicating the success of the course organization removal operation.
     * @throws DataServiceException if the course organization entry does not exist.
     */
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