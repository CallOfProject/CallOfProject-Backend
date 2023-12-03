package callofproject.dev.authentication.controller;

import callofproject.dev.authentication.dto.*;
import callofproject.dev.authentication.dto.client.CompanySaveDTO;
import callofproject.dev.authentication.dto.client.CourseOrganizationSaveDTO;
import callofproject.dev.authentication.dto.client.CourseSaveDTO;
import callofproject.dev.authentication.dto.client.UniversitySaveDTO;
import callofproject.dev.authentication.service.IEnvironmentClient;
import callofproject.dev.authentication.service.UserManagementService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.apache.hc.core5.http.HttpStatus.SC_OK;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

@RequestMapping("api/users")
@RestController
@SecurityRequirement(name = "Authorization")
public class UserManagementController
{
    private final UserManagementService m_service;
    private final IEnvironmentClient m_environmentClient;

    public UserManagementController(UserManagementService service, IEnvironmentClient environmentClient)
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
        return subscribe(() -> ok(new MessageResponseDTO<UserDTO>("User found Successfully!", SC_OK, m_service.findUserByUsername(username))),
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

    @PostMapping("save/course-organization")
    public ResponseEntity<Object> saveCourseOrganization(@RequestBody CourseOrganizationUpsertDTO dto)
    {
        return subscribe(() -> ok(m_service.upsertCourseOrganization(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }


    @PostMapping("save/ms/university")
    public ResponseEntity<Object> saveUniversity(@RequestBody UniversitySaveDTO dto)
    {
        return subscribe(() -> ok(m_environmentClient.saveUniversity(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    @PostMapping("save/ms/course")
    public ResponseEntity<Object> saveCourse(@RequestBody CourseSaveDTO organizationDTO)
    {
        return subscribe(() -> ok(m_environmentClient.saveCourse(organizationDTO)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    @PostMapping("save/ms/company")
    public ResponseEntity<Object> saveCompany(@RequestBody CompanySaveDTO companyDTO)
    {
        return subscribe(() -> ok(m_environmentClient.saveCompany(companyDTO)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    @PostMapping("save/ms/course-organization")
    public ResponseEntity<Object> saveCourseOrganization(@RequestBody CourseOrganizationSaveDTO courseOrganizationSaveDTO)
    {
        return subscribe(() -> ok(m_environmentClient.saveCourseOrganization(courseOrganizationSaveDTO)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }
}
