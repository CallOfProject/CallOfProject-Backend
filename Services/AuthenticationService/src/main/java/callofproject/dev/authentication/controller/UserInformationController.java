package callofproject.dev.authentication.controller;

import callofproject.dev.authentication.dto.environments.CourseUpsertDTO;
import callofproject.dev.authentication.dto.environments.EducationUpsertDTO;
import callofproject.dev.authentication.dto.environments.ExperienceUpsertDTO;
import callofproject.dev.authentication.dto.environments.LinkUpsertDTO;
import callofproject.dev.authentication.service.UserInformationService;
import callofproject.dev.data.common.clas.ErrorMessage;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/user-info")
@SecurityRequirement(name = "Authorization")
public class UserInformationController
{
    private final UserInformationService m_service;

    public UserInformationController(UserInformationService service)
    {
        m_service = service;
    }

    /**
     * Save education to user.
     *
     * @param dto represent the education information
     * @return boolean value success or not
     */
    @PostMapping("upsert/education")
    public ResponseEntity<Object> saveEducation(@RequestBody EducationUpsertDTO dto)
    {
        return subscribe(() -> ok(m_service.upsertEducation(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Save experience to user.
     *
     * @param dto represent the experience information
     * @return boolean value success or not
     */
    @PostMapping("upsert/experience")
    public ResponseEntity<Object> saveExperience(@RequestBody ExperienceUpsertDTO dto)
    {
        return subscribe(() -> ok(m_service.upsertExperience(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Save link to user.
     *
     * @param dto represent the link information
     * @return boolean value success or not
     */
    @PostMapping("upsert/link")
    public ResponseEntity<Object> saveLink(@RequestBody LinkUpsertDTO dto)
    {
        return subscribe(() -> ok(m_service.upsertLink(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Save course to user.
     *
     * @param dto represent the course information
     * @return boolean value success or not
     */
    @PostMapping("upsert/course")
    public ResponseEntity<Object> saveCourse(@RequestBody CourseUpsertDTO dto)
    {
        return subscribe(() -> ok(m_service.upsertCourse(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Remove education from user.
     *
     * @param userId represent the user id
     * @param id     represent the education id
     * @return boolean value success or not
     */
    @DeleteMapping("delete/education")
    public ResponseEntity<Object> removeEducation(@RequestParam("uid") UUID userId, @RequestParam("id") UUID id)
    {

        return subscribe(() -> ok(m_service.removeEducation(userId, id)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Remove experience from user.
     *
     * @param userId represent the user id
     * @param id     represent the experience id
     * @return boolean value success or not
     */
    @DeleteMapping("delete/experience")
    public ResponseEntity<Object> removeExperience(@RequestParam("uid") UUID userId, @RequestParam("id") UUID id)
    {
        return subscribe(() -> ok(m_service.removeExperience(userId, id)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Remove link from user.
     *
     * @param userId represent the user id
     * @param id     represent the link id
     * @return boolean value success or not
     */
    @DeleteMapping("delete/link")
    public ResponseEntity<Object> removeLink(@RequestParam("uid") UUID userId, @RequestParam("id") long id)
    {
        return subscribe(() -> ok(m_service.removeLink(userId, id)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Remove course from user.
     *
     * @param userId represent the user id
     * @param id     represent the course id
     * @return boolean value success or not
     */
    @DeleteMapping("delete/course")
    public ResponseEntity<Object> removeCourse(@RequestParam("uid") UUID userId, @RequestParam("id") UUID id)
    {
        return subscribe(() -> ok(m_service.removeCourse(userId, id)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }
}
