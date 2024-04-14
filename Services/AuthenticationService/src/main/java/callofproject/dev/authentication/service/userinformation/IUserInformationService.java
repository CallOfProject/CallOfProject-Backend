package callofproject.dev.authentication.service.userinformation;

import callofproject.dev.authentication.dto.environments.*;
import callofproject.dev.data.common.clas.ResponseMessage;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Service class for handling user information operations.
 */
public interface IUserInformationService
{
    /**
     * Upsert education.
     *
     * @param dto represents the education upsert dto
     * @return the response message
     */
    ResponseMessage<Object> saveEducation(EducationCreateDTO dto);

    ResponseMessage<Object> updateEducation(EducationUpdateDTO dto);

    /**
     * Upsert experience.
     *
     * @param dto represents the experience upsert dto
     * @return the response message
     */
    ResponseMessage<Object> saveExperience(ExperienceCreateDTO dto);

    /**
     * Update experience.
     *
     * @param dto represents the experience update dto
     * @return the response message
     */
    ResponseMessage<Object> updateExperience(ExperienceUpdateDTO dto);

    /**
     * Upsert course.
     *
     * @param dto represents the course upsert dto
     * @return the response message
     */
    ResponseMessage<Object> saveCourse(CourseCreateDTO dto);


    /**
     * Update course.
     *
     * @param dto represents the course update dto
     * @return the response message
     */
    ResponseMessage<Object> updateCourse(CourseUpdateDTO dto);

    /**
     * Upsert link.
     *
     * @param dto represents the link upsert dto
     * @return the response message
     */
    ResponseMessage<Object> saveLink(LinkCreateDTO dto);

    /**
     * Update link.
     *
     * @param dto represents the link update dto
     * @return the response message
     */
    ResponseMessage<Object> updateLink(LinkUpdateDTO dto);

    /**
     * Remove education.
     *
     * @param userId represents the user id
     * @param id     represents the education id
     * @return the response message
     */
    ResponseMessage<Object> removeEducation(UUID userId, UUID id);

    /**
     * Remove course.
     *
     * @param userId represents the user id
     * @param id     represents the course id
     * @return the response message
     */
    ResponseMessage<Object> removeCourse(UUID userId, UUID id);

    /**
     * Remove experience.
     *
     * @param userId represents the user id
     * @param id     represents the experience id
     * @return the response message
     */
    ResponseMessage<Object> removeExperience(UUID userId, UUID id);

    /**
     * Remove link.
     *
     * @param userId represents the user id
     * @param id     represents the link id
     * @return the response message
     */
    ResponseMessage<Object> removeLink(UUID userId, long id);

    /**
     * Remove course organization.
     *
     * @param userId represents the user id
     * @param id     represents the course organization id
     * @return the response message
     */
    ResponseMessage<Object> removeCourseOrganization(UUID userId, UUID id);

    /**
     * Upload user profile photo.
     *
     * @param userId represents the user id
     * @param file   represents user profile photo
     * @return the response message
     */
    ResponseMessage<Object> uploadUserProfilePhoto(UUID userId, MultipartFile file);

    /**
     * Upload user cv.
     *
     * @param userId represents the user id
     * @param file   represents user cv
     * @return the response message
     */
    ResponseMessage<Object> uploadCV(UUID userId, MultipartFile file);
}
