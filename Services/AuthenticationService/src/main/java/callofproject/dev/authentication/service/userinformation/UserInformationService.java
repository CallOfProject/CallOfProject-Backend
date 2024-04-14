package callofproject.dev.authentication.service.userinformation;

import callofproject.dev.authentication.dto.environments.*;
import callofproject.dev.data.common.clas.ResponseMessage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;

/**
 * Service class for managing user information, including education, experience, courses, links, and more.
 */
@Service
@Lazy
public class UserInformationService implements IUserInformationService
{
    private final UserInformationServiceCallback m_serviceCallback;

    /**
     * Constructor for UserInformationService.
     *
     * @param serviceCallback The UserInformationServiceCallback to interact with user information operations.
     */
    public UserInformationService(UserInformationServiceCallback serviceCallback)
    {
        m_serviceCallback = serviceCallback;
    }

    /**
     * Upsert education with given dto class.
     *
     * @param dto represent the dto class
     * @return MessageResponseDTO.
     */
    @Override
    public ResponseMessage<Object> saveEducation(EducationCreateDTO dto)
    {
        return doForDataService(() -> m_serviceCallback.saveEducationCallback(dto), "Education cannot be upserted!");
    }

    /**
     * Update education with given dto class.
     *
     * @param dto represent the dto class
     * @return MessageResponseDTO.
     */
    @Override
    public ResponseMessage<Object> updateEducation(EducationUpdateDTO dto)
    {
        return doForDataService(() -> m_serviceCallback.updateEducationCallback(dto), "Education cannot be updated!");
    }

    /**
     * Upsert experience with given dto class.
     *
     * @param dto represent the dto class
     * @return MessageResponseDTO.
     */
    @Override
    public ResponseMessage<Object> saveExperience(ExperienceCreateDTO dto)
    {
        return doForDataService(() -> m_serviceCallback.saveExperienceCallback(dto), "Experience cannot be upserted!");
    }

    /**
     * Update experience with given dto class.
     *
     * @param dto represent the dto class
     * @return MessageResponseDTO.
     */
    @Override
    public ResponseMessage<Object> updateExperience(ExperienceUpdateDTO dto)
    {
        return doForDataService(() -> m_serviceCallback.updateExperienceCallback(dto), "Experience cannot be updated!");
    }

    /**
     * Upsert course with given dto class.
     *
     * @param dto represent the dto class
     * @return MessageResponseDTO.
     */
    @Override
    public ResponseMessage<Object> saveCourse(CourseCreateDTO dto)
    {
        return doForDataService(() -> m_serviceCallback.saveCourseCallback(dto), "Course cannot be upserted!");
    }


    /**
     * Update course with given dto class.
     *
     * @param dto represent the dto class
     * @return MessageResponseDTO.
     */
    @Override
    public ResponseMessage<Object> updateCourse(CourseUpdateDTO dto)
    {
        return doForDataService(() -> m_serviceCallback.updateCourseCallback(dto), "Course cannot be updated!");
    }

    /**
     * Upsert link with given dto class.
     *
     * @param dto represent the dto class
     * @return MessageResponseDTO.
     */
    @Override
    public ResponseMessage<Object> saveLink(LinkCreateDTO dto)
    {
        return doForDataService(() -> m_serviceCallback.saveLinkCallback(dto), "Link cannot be upserted!");
    }


    /**
     * Update link with given dto class.
     *
     * @param dto represent the dto class
     * @return MessageResponseDTO.
     */
    @Override
    public ResponseMessage<Object> updateLink(LinkUpdateDTO dto)
    {
        return doForDataService(() -> m_serviceCallback.updateLinkCallback(dto), "Link cannot be updated!");
    }

    /**
     * Remove education with given id.
     *
     * @param userId represent the user id.
     * @param id     represent the education id.
     * @return MessageResponseDTO.
     */
    @Override
    public ResponseMessage<Object> removeEducation(UUID userId, UUID id)
    {
        return doForDataService(() -> m_serviceCallback.removeEducationCallback(userId, id), "Education cannot be removed!");
    }

    /**
     * Remove course with given id.
     *
     * @param userId represent the user id.
     * @param id     represent the course id.
     * @return MessageResponseDTO.
     */
    @Override
    public ResponseMessage<Object> removeCourse(UUID userId, UUID id)
    {
        return doForDataService(() -> m_serviceCallback.removeCourseCallback(userId, id), "Course cannot be removed!");
    }

    /**
     * Remove experience with given id.
     *
     * @param userId represent the user id.
     * @param id     represent the experience id.
     * @return MessageResponseDTO.
     */
    @Override
    public ResponseMessage<Object> removeExperience(UUID userId, UUID id)
    {
        return doForDataService(() -> m_serviceCallback.removeExperienceCallback(userId, id), "Experience cannot be removed!");
    }

    /**
     * Remove link with given id.
     *
     * @param userId represent the user id.
     * @param id     represent the link id.
     * @return MessageResponseDTO.
     */
    @Override
    public ResponseMessage<Object> removeLink(UUID userId, long id)
    {
        return doForDataService(() -> m_serviceCallback.removeLinkCallback(userId, id), "Link cannot be removed!");
    }

    /**
     * Remove course organization with given id.
     *
     * @param userId represent the user id.
     * @param id     represent the course organization id.
     * @return MessageResponseDTO.
     */
    @Override
    public ResponseMessage<Object> removeCourseOrganization(UUID userId, UUID id)
    {
        return doForDataService(() -> m_serviceCallback.removeCourseOrganizationCallback(userId, id), "Course organization cannot be removed!");
    }


    /**
     * Uploads a user profile photo for the given user ID.
     *
     * @param userId The UUID of the user whose profile photo is being uploaded.
     * @param file   The MultipartFile containing the user profile photo.
     * @return A ResponseMessage indicating the success of the profile photo upload operation.
     */
    @Override
    public ResponseMessage<Object> uploadUserProfilePhoto(UUID userId, MultipartFile file)
    {
        return doForDataService(() -> m_serviceCallback.uploadUserProfilePhotoCallback(userId, file), "Profile photo cannot be uploaded!");
    }


    /**
     * Uploads a user CV for the given user ID.
     *
     * @param userId The UUID of the user whose CV is being uploaded.
     * @param file   The MultipartFile containing the user CV.
     * @return A ResponseMessage indicating the success of the CV upload operation.
     */
    @Override
    public ResponseMessage<Object> uploadCV(UUID userId, MultipartFile file)
    {
        return doForDataService(() -> m_serviceCallback.uploadCVCallback(userId, file), "CV cannot be uploaded!");
    }
}