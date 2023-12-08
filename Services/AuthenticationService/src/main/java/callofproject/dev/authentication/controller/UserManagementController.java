package callofproject.dev.authentication.controller;

import callofproject.dev.authentication.dto.ErrorMessage;
import callofproject.dev.authentication.dto.UserProfileUpdateDTO;
import callofproject.dev.authentication.dto.UserSignUpRequestDTO;
import callofproject.dev.authentication.dto.environments.CourseUpsertDTO;
import callofproject.dev.authentication.dto.environments.EducationUpsertDTO;
import callofproject.dev.authentication.dto.environments.ExperienceUpsertDTO;
import callofproject.dev.authentication.dto.environments.LinkUpsertDTO;
import callofproject.dev.authentication.service.IEnvironmentClientService;
import callofproject.dev.authentication.service.UserManagementService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

@RequestMapping("api/users")
@RestController
@SecurityRequirement(name = "Authorization")
public class UserManagementController
{
    private final UserManagementService m_service;
    private final IEnvironmentClientService m_environmentClient;

    public UserManagementController(UserManagementService service, IEnvironmentClientService environmentClient)
    {
        m_service = service;
        m_environmentClient = environmentClient;
    }

    /**
     * Save user and give USER_ROLE automatically
     *
     * @param dto is userSignUpDTO
     * @return if success UserDTO else return Error Message
     */
    @PostMapping("save/user")
    public ResponseEntity<Object> saveUser(@RequestBody UserSignUpRequestDTO dto)
    {
        return subscribe(() -> ok(m_service.saveUser(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Find user with given username
     *
     * @param username is username of user
     * @return UserDTO wrapped in MessageResponseDTO
     */
    @GetMapping("find/user/username")
    public ResponseEntity<Object> findUserByUsername(@RequestParam("n") String username)
    {
        return subscribe(() -> ok(m_service.findUserByUsername(username)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Find user with given page and word parameters.
     *
     * @param word represent the containing in the username
     * @param page is page
     * @return UsersDTO
     */
    @GetMapping("find/user/contains")
    public ResponseEntity<Object> findUserByWordContains(@RequestParam(value = "word", required = false) String word,
                                                         @RequestParam("p") int page)
    {
        return subscribe(() -> ok(m_service.findAllUsersPageableByContainsWord(page, word)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    @PutMapping("update/user/profile")
    public ResponseEntity<Object> updateUserProfile(@RequestBody UserProfileUpdateDTO dto)
    {
        return subscribe(() -> ok(m_service.upsertUserProfile(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    @GetMapping("find/user/profile/username")
    public ResponseEntity<Object> findUserProfileByUsername(@RequestParam("n") String username)
    {
        return subscribe(() -> ok(m_service.findUserProfileByUsername(username)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    @PostMapping("save/education")
    public ResponseEntity<Object> saveEducation(@RequestBody EducationUpsertDTO dto)
    {
        return subscribe(() -> ok(m_service.upsertEducation(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    @PostMapping("save/experience")
    public ResponseEntity<Object> saveExperience(@RequestBody ExperienceUpsertDTO dto)
    {
        return subscribe(() -> ok(m_service.upsertExperience(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    @PostMapping("save/link")
    public ResponseEntity<Object> saveLink(@RequestBody LinkUpsertDTO dto)
    {
        return subscribe(() -> ok(m_service.upsertLink(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    @PostMapping("save/course")
    public ResponseEntity<Object> saveCourse(@RequestBody CourseUpsertDTO dto)
    {
        return subscribe(() -> ok(m_service.upsertCourse(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }


    @DeleteMapping("delete/education")
    public ResponseEntity<Object> removeEducation(@RequestParam("uid") UUID userId,@RequestParam("eid") UUID id)
    {

        return subscribe(() -> ok(m_service.removeEducation(userId, id)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }


}
