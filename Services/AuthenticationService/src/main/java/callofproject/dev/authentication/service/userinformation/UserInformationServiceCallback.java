package callofproject.dev.authentication.service.userinformation;

import callofproject.dev.authentication.dto.client.CompanySaveDTO;
import callofproject.dev.authentication.dto.client.CourseOrganizationSaveDTO;
import callofproject.dev.authentication.dto.client.CourseSaveDTO;
import callofproject.dev.authentication.dto.client.UniversitySaveDTO;
import callofproject.dev.authentication.dto.environments.*;
import callofproject.dev.authentication.mapper.MapperConfiguration;
import callofproject.dev.authentication.service.IEnvironmentClientService;
import callofproject.dev.authentication.service.S3Service;
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

@Service
@Lazy
public class UserInformationServiceCallback
{

    private final UserManagementServiceHelper m_serviceHelper;
    private final MatchServiceHelper m_matchServiceHelper;
    private final IEnvironmentClientService m_environmentClient;
    private final MapperConfiguration m_mapperConfig;
    private final S3Service m_s3Service;
    @Value("${application.cv-bucket.name}")
    private String m_cvBucketName;

    /**
     * Constructs a new UserInformationServiceCallback with the given UserManagementServiceHelper, MatchServiceHelper,
     * IEnvironmentClientService, MapperConfiguration, and S3Service.
     *
     * @param serviceHelper      The UserManagementServiceHelper to be used by this service.
     * @param matchServiceHelper The MatchServiceHelper to be used by this service.
     * @param environmentClient  The IEnvironmentClientService to be used by this service.
     * @param mapperConfig       The MapperConfiguration to be used by this service.
     * @param s3Service          The S3Service to be used by this service.
     */
    public UserInformationServiceCallback(UserManagementServiceHelper serviceHelper, MatchServiceHelper matchServiceHelper, IEnvironmentClientService environmentClient, MapperConfiguration mapperConfig, S3Service s3Service)
    {
        m_serviceHelper = serviceHelper;
        m_matchServiceHelper = matchServiceHelper;
        m_environmentClient = environmentClient;
        m_mapperConfig = mapperConfig;
        m_s3Service = s3Service;
    }


    /**
     * Adds or updates educational information for a user based on the provided EducationUpsertDTO.
     *
     * @param dto The DTO containing education information to be upserted.
     * @return A ResponseMessage indicating the success of the education upsert operation.
     * @throws DataServiceException if the user or education information does not exist.
     */
    public ResponseMessage<Object> saveEducationCallback(EducationCreateDTO dto)
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
     * Updates educational information for a user based on the provided EducationUpdateDTO.
     *
     * @param dto The DTO containing education information to be updated.
     * @return A ResponseMessage indicating the success of the education update operation.
     */
    public ResponseMessage<Object> updateEducationCallback(EducationUpdateDTO dto)
    {
        var education = m_serviceHelper.getEducationServiceHelper().findByIdEducation(dto.getEducationId());

        if (education.isEmpty())
            throw new DataServiceException("Education does not exists!");

        education.get().setSchoolName(dto.getSchoolName());
        education.get().setDepartment(dto.getDepartment());
        education.get().setDescription(dto.getDescription());
        education.get().setStartDate(dto.getStartDate());
        education.get().setFinishDate(dto.getFinishDate());
        education.get().setContinue(dto.isContinue());
        education.get().setGpa(dto.getGpa());

        var updatedEducation = m_serviceHelper.getEducationServiceHelper().saveEducation(education.get());
        var educationDTO = m_mapperConfig.educationMapper.toEducationDTO(updatedEducation);
        return new ResponseMessage<>("Education updated successfully!", 200, educationDTO);
    }

    /**
     * Adds or updates professional experience for a user based on the provided ExperienceUpsertDTO.
     *
     * @param dto The DTO containing experience information to be upserted.
     * @return A ResponseMessage indicating the success of the experience upsert operation.
     * @throws DataServiceException if the user or experience information does not exist.
     */
    public ResponseMessage<Object> saveExperienceCallback(ExperienceCreateDTO dto)
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
     * Updates educational information for a user based on the provided EducationUpdateDTO.
     *
     * @param dto The DTO containing education information to be updated.
     * @return A ResponseMessage indicating the success of the education update operation.
     */
    public ResponseMessage<Object> updateExperienceCallback(ExperienceUpdateDTO dto)
    {
        var experience = m_serviceHelper.getExperienceServiceHelper().findById(dto.getExperienceId());

        if (experience.isEmpty()) // unexpected case
            throw new DataServiceException("Experience does not exists!");

        experience.get().setCompanyName(dto.getCompanyName());
        experience.get().setDescription(dto.getDescription());
        experience.get().setCompanyWebsiteLink(dto.getCompanyWebsite());
        experience.get().setStartDate(dto.getStartDate());
        experience.get().setFinishDate(dto.getFinishDate());
        experience.get().setContinue(dto.isContinue());
        experience.get().setJobDefinition(dto.getJobDefinition());

        var updatedExperience = m_serviceHelper.getExperienceServiceHelper().saveExperience(experience.get());
        var experienceDTO = m_mapperConfig.experienceMapper.toExperienceDTO(updatedExperience);
        return new ResponseMessage<>("Experience updated successfully!", 200, experienceDTO);
    }


    /**
     * Adds or updates course information for a user based on the provided CourseUpsertDTO.
     *
     * @param dto The DTO containing course information to be upserted.
     * @return A ResponseMessage indicating the success of the course upsert operation.
     * @throws DataServiceException if the user or course information does not exist.
     */
    public ResponseMessage<Object> saveCourseCallback(CourseCreateDTO dto)
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
     * Updates course information for a user based on the provided CourseUpdateDTO.
     *
     * @param dto The DTO containing course information to be updated.
     * @return A ResponseMessage indicating the success of the course update operation.
     */
    public ResponseMessage<Object> updateCourseCallback(CourseUpdateDTO dto)
    {
        var course = m_serviceHelper.getCourseServiceHelper().findById(dto.getCourseId());

        if (course.isEmpty()) // unexpected case
            throw new DataServiceException("Course does not exists!");


        if (!course.get().getCourseOrganization().getCourseOrganizationName().equals(convert(dto.getOrganizator())))
        {
            var organizator = m_serviceHelper.getCourseOrganizationServiceHelper()
                    .findByCourseOrganizationNameContainsIgnoreCase(dto.getOrganizator());

            var organization = organizator.orElseGet(() -> m_serviceHelper.getCourseOrganizationServiceHelper()
                    .saveCourseOrganization(new CourseOrganization(dto.getOrganizator())));

            course.get().setCourseOrganization(organization);
        }

        course.get().setCourseName(dto.getCourseName());
        course.get().setStartDate(dto.getStartDate());
        course.get().setFinishDate(dto.getFinishDate());
        course.get().setContinue(dto.isContinue());
        course.get().setDescription(dto.getDescription());

        var updatedCourse = m_serviceHelper.getCourseServiceHelper().saveCourse(course.get());
        var courseDTO = m_mapperConfig.courseMapper.toCourseDTO(updatedCourse);
        return new ResponseMessage<>("Experience updated successfully!", 200, courseDTO);
    }

    /**
     * Adds or updates a link for a user based on the provided LinkUpsertDTO.
     *
     * @param dto The DTO containing link information to be upserted.
     * @return A ResponseMessage indicating the success of the link upsert operation.
     */
    public ResponseMessage<Object> saveLinkCallback(LinkCreateDTO dto)
    {
        var user = getUserProfile(dto.userId());

        var upsertedLink = doForDataService(() -> m_serviceHelper.getLinkServiceHelper().saveLink(m_mapperConfig.linkMapper.toLink(dto)), "Link cannot be upserted!");

        user.addLink(upsertedLink);
        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(user);

        return new ResponseMessage<>("Link upserted successfully!", 200, upsertedLink);
    }


    /**
     * Updates a link for a user based on the provided LinkUpdateDTO.
     *
     * @param dto The DTO containing link information to be updated.
     * @return A ResponseMessage indicating the success of the link update operation.
     */
    public ResponseMessage<Object> updateLinkCallback(LinkUpdateDTO dto)
    {
        var link = m_serviceHelper.getLinkServiceHelper().findById(dto.linkId());

        if (link.isEmpty())
            throw new DataServiceException("Link does not exists!");

        link.get().setLink(dto.link());
        link.get().setLinkTitle(dto.linkTitle());

        var updatedLink = m_serviceHelper.getLinkServiceHelper().saveLink(link.get());
        var linkDTO = m_mapperConfig.linkMapper.toLinkDTO(updatedLink);
        return new ResponseMessage<>("Link updated successfully!", 200, linkDTO);
    }

    /**
     * Removes an education entry for a user based on provided identifiers.
     *
     * @param userId The UUID of the user.
     * @param id     The UUID of the education entry to be removed.
     * @return A ResponseMessage indicating the success of the education removal operation.
     * @throws DataServiceException if the education entry does not exist.
     */
    public ResponseMessage<Object> removeEducationCallback(UUID userId, UUID id)
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
    public ResponseMessage<Object> removeCourseCallback(UUID userId, UUID id)
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
    public ResponseMessage<Object> removeExperienceCallback(UUID userId, UUID id)
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
    public ResponseMessage<Object> removeLinkCallback(UUID userId, long id)
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
    public ResponseMessage<Object> removeCourseOrganizationCallback(UUID userId, UUID id)
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

    /**
     * Uploads a user profile photo for the given user ID.
     *
     * @param userId The UUID of the user whose profile photo is being uploaded.
     * @param file   The MultipartFile containing the user profile photo.
     * @return A ResponseMessage indicating the success of the profile photo upload operation.
     */
    public ResponseMessage<Object> uploadUserProfilePhotoCallback(UUID userId, MultipartFile file)
    {
        var userProfile = getUserProfile(userId);

        var fileNameSplit = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        var extension = fileNameSplit[fileNameSplit.length - 1];
        var fileName = "pp_" + userProfile.getUser().getUserId() + "_" + userProfile.getUserProfileId() + "." + extension;

        var profilePhoto = m_s3Service.uploadToS3WithMultiPartFileV2(file, fileName, Optional.empty());

        userProfile.setProfilePhoto(profilePhoto);

        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(userProfile);

        return new ResponseMessage<>("Profile photo uploaded successfully!", 200, profilePhoto);
    }

    /**
     * Uploads a user CV for the given user ID.
     *
     * @param userId The UUID of the user whose CV is being uploaded.
     * @param file   The MultipartFile containing the user CV.
     * @return A ResponseMessage indicating the success of the CV upload operation.
     */
    public ResponseMessage<Object> uploadCVCallback(UUID userId, MultipartFile file)
    {
        var userProfile = getUserProfile(userId);
        var fileNameSplit = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        var extension = fileNameSplit[fileNameSplit.length - 1];
        var fileName = "cv_" + userProfile.getUser().getUserId() + "." + extension;
        var cv = m_s3Service.uploadToS3WithMultiPartFileV2(file, fileName, Optional.of(m_cvBucketName));
        userProfile.setCv(cv);
        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(userProfile);
        return new ResponseMessage<>("CV uploaded successfully!", 200, cv);
    }

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
}
