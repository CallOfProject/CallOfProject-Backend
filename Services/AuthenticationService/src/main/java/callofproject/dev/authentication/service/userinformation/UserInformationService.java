package callofproject.dev.authentication.service.userinformation;

import callofproject.dev.authentication.dto.environments.CourseUpsertDTO;
import callofproject.dev.authentication.dto.environments.EducationUpsertDTO;
import callofproject.dev.authentication.dto.environments.ExperienceUpsertDTO;
import callofproject.dev.authentication.dto.environments.LinkUpsertDTO;
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
    public ResponseMessage<Object> upsertEducation(EducationUpsertDTO dto)
    {
        return doForDataService(() -> m_serviceCallback.upsertEducationCallback(dto), "Education cannot be upserted!");
    }

    /**
     * Upsert experience with given dto class.
     *
     * @param dto represent the dto class
     * @return MessageResponseDTO.
     */
    @Override
    public ResponseMessage<Object> upsertExperience(ExperienceUpsertDTO dto)
    {
        return doForDataService(() -> m_serviceCallback.upsertExperienceCallback(dto), "Experience cannot be upserted!");
    }

    /**
     * Upsert course with given dto class.
     *
     * @param dto represent the dto class
     * @return MessageResponseDTO.
     */
    @Override
    public ResponseMessage<Object> upsertCourse(CourseUpsertDTO dto)
    {
        return doForDataService(() -> m_serviceCallback.upsertCourseCallback(dto), "Course cannot be upserted!");
    }

    /**
     * Upsert link with given dto class.
     *
     * @param dto represent the dto class
     * @return MessageResponseDTO.
     */
    @Override
    public ResponseMessage<Object> upsertLink(LinkUpsertDTO dto)
    {
        return doForDataService(() -> m_serviceCallback.upsertLinkCallback(dto), "Link cannot be upserted!");
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