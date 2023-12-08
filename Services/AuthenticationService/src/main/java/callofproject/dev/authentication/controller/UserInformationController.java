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

    @PostMapping("upsert/education")
    public ResponseEntity<Object> saveEducation(@RequestBody EducationUpsertDTO dto)
    {
        return subscribe(() -> ok(m_service.upsertEducation(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    @PostMapping("upsert/experience")
    public ResponseEntity<Object> saveExperience(@RequestBody ExperienceUpsertDTO dto)
    {
        return subscribe(() -> ok(m_service.upsertExperience(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    @PostMapping("upsert/link")
    public ResponseEntity<Object> saveLink(@RequestBody LinkUpsertDTO dto)
    {
        return subscribe(() -> ok(m_service.upsertLink(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    @PostMapping("upsert/course")
    public ResponseEntity<Object> saveCourse(@RequestBody CourseUpsertDTO dto)
    {
        return subscribe(() -> ok(m_service.upsertCourse(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }


    @DeleteMapping("delete/education")
    public ResponseEntity<Object> removeEducation(@RequestParam("uid") UUID userId, @RequestParam("id") UUID id)
    {

        return subscribe(() -> ok(m_service.removeEducation(userId, id)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    @DeleteMapping("delete/experience")
    public ResponseEntity<Object> removeExperience(@RequestParam("uid") UUID userId, @RequestParam("id") UUID id)
    {
        return subscribe(() -> ok(m_service.removeExperience(userId, id)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    @DeleteMapping("delete/link")
    public ResponseEntity<Object> removeLink(@RequestParam("uid") UUID userId, @RequestParam("id") long id)
    {
        return subscribe(() -> ok(m_service.removeLink(userId, id)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    @DeleteMapping("delete/course")
    public ResponseEntity<Object> removeCourse(@RequestParam("uid") UUID userId, @RequestParam("id") UUID id)
    {
        return subscribe(() -> ok(m_service.removeCourse(userId, id)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }
}
