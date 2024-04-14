package callofproject.dev.authentication.controller;

import callofproject.dev.authentication.dto.environments.*;
import callofproject.dev.authentication.service.userinformation.UserInformationService;
import callofproject.dev.data.common.clas.ErrorMessage;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;


/**
 * Authentication Controller
 * Copyleft (C), 2023, Cal-of-Project-Teams Developers.
 * All Rights free.
 */
@RestController
@RequestMapping("api/auth/user-info")
@SecurityRequirement(name = "Authorization")
public class UserInformationController
{
    private final UserInformationService m_service;

    /**
     * Constructor for the UserInformationController class.
     * It is used to inject dependencies into the controller.
     *
     * @param service The UserInformationService object to be injected.
     */
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
    @PostMapping("save/education")
    public ResponseEntity<Object> saveEducation(@RequestBody EducationCreateDTO dto)
    {
        return subscribe(() -> ok(m_service.saveEducation(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Update education to user.
     *
     * @param dto represent the education information
     * @return boolean value success or not
     */
    @PutMapping("update/education")
    public ResponseEntity<Object> updateEducation(@RequestBody EducationUpdateDTO dto)
    {
        return subscribe(() -> ok(m_service.updateEducation(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Save experience to user.
     *
     * @param dto represent the experience information
     * @return boolean value success or not
     */
    @PostMapping("save/experience")
    public ResponseEntity<Object> saveExperience(@RequestBody ExperienceCreateDTO dto)
    {
        return subscribe(() -> ok(m_service.saveExperience(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }


    /**
     * Update experience to user.
     *
     * @param dto represent the experience information
     * @return boolean value success or not
     */
    @PutMapping("update/experience")
    public ResponseEntity<Object> updateExperience(@RequestBody ExperienceUpdateDTO dto)
    {
        return subscribe(() -> ok(m_service.updateExperience(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Save link to user.
     *
     * @param dto represent the link information
     * @return boolean value success or not
     */
    @PostMapping("save/link")
    public ResponseEntity<Object> saveLink(@RequestBody LinkCreateDTO dto)
    {
        return subscribe(() -> ok(m_service.saveLink(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Update link to user.
     *
     * @param dto represent the link information
     * @return boolean value success or not
     */
    @PutMapping("update/link")
    public ResponseEntity<Object> updateLink(@RequestBody LinkUpdateDTO dto)
    {
        return subscribe(() -> ok(m_service.updateLink(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Save course to user.
     *
     * @param dto represent the course information
     * @return boolean value success or not
     */
    @PostMapping("save/course")
    public ResponseEntity<Object> saveCourse(@RequestBody CourseCreateDTO dto)
    {
        return subscribe(() -> ok(m_service.saveCourse(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Update course to user.
     *
     * @param dto represent the course information
     * @return boolean value success or not
     */
    @PutMapping("update/course")
    public ResponseEntity<Object> updateCourse(@RequestBody CourseUpdateDTO dto)
    {
        return subscribe(() -> ok(m_service.updateCourse(dto)),
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

    /**
     * Upload user profile photo.
     *
     * @param userId represent the user id
     * @param file   represent the file
     * @return boolean value success or not
     */
    @PostMapping("upload/profile-photo")
    public ResponseEntity<Object> uploadUserProfilePhoto(@RequestParam("uid") UUID userId, @RequestParam MultipartFile file)
    {
        return subscribe(() -> ok(m_service.uploadUserProfilePhoto(userId, file)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }


    /**
     * Upload user cv.
     *
     * @param userId represent the user id
     * @param file   represent the file
     * @return boolean value success or not
     */
    @PostMapping("upload/cv")
    public ResponseEntity<Object> uploadUserCV(@RequestParam("uid") UUID userId, @RequestParam MultipartFile file)
    {
        return subscribe(() -> ok(m_service.uploadCV(userId, file)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }
}
