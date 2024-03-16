package callofproject.dev.authentication.service.userinformation;

import callofproject.dev.authentication.dto.environments.CourseUpsertDTO;
import callofproject.dev.authentication.dto.environments.EducationUpsertDTO;
import callofproject.dev.authentication.dto.environments.ExperienceUpsertDTO;
import callofproject.dev.authentication.dto.environments.LinkUpsertDTO;
import callofproject.dev.data.common.clas.ResponseMessage;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface IUserInformationService
{
    ResponseMessage<Object> upsertEducation(EducationUpsertDTO dto);

    ResponseMessage<Object> upsertExperience(ExperienceUpsertDTO dto);

    ResponseMessage<Object> upsertCourse(CourseUpsertDTO dto);

    ResponseMessage<Object> upsertLink(LinkUpsertDTO dto);

    ResponseMessage<Object> removeEducation(UUID userId, UUID id);

    ResponseMessage<Object> removeCourse(UUID userId, UUID id);

    ResponseMessage<Object> removeExperience(UUID userId, UUID id);

    ResponseMessage<Object> removeLink(UUID userId, long id);

    ResponseMessage<Object> removeCourseOrganization(UUID userId, UUID id);

    ResponseMessage<Object> uploadUserProfilePhoto(UUID userId, MultipartFile file);

    ResponseMessage<Object> uploadCV(UUID userId, MultipartFile file);
}
