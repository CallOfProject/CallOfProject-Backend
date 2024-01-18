package callofproject.dev.authentication.controller;

import callofproject.dev.authentication.dto.UserProfileUpdateDTO;
import callofproject.dev.authentication.dto.UserSignUpRequestDTO;
import callofproject.dev.authentication.service.UserManagementService;
import callofproject.dev.data.common.clas.ErrorMessage;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;


/**
 * Authentication Controller
 * Copyleft (C), 2023, Cal-of-Project-Teams Developers.
 * All Rights free.
 */
@RequestMapping("api/auth/users")
@RestController
@SecurityRequirement(name = "Authorization")
public class UserManagementController
{
    private final UserManagementService m_service;


    /**
     * Constructor for the UserManagementController class.
     * It is used to inject dependencies into the controller.
     *
     * @param service The UserManagementService object to be injected.
     */
    public UserManagementController(UserManagementService service)
    {
        m_service = service;
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

    /**
     * Find user with given id
     *
     * @param dto is update user profile dto
     * @return UserDTO wrapped in MessageResponseDTO
     */
    @PutMapping("update/user/profile")
    public ResponseEntity<Object> updateUserProfile(@RequestBody UserProfileUpdateDTO dto)
    {
        return subscribe(() -> ok(m_service.upsertUserProfile(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Find user Profile with username
     *
     * @param username is username of user
     * @return ResponseMessage wrapped in UserProfileDTO
     */
    @GetMapping("find/user/profile/username")
    public ResponseEntity<Object> findUserProfileByUsername(@RequestParam("uname") String username)
    {
        return subscribe(() -> ok(m_service.findUserProfileByUsername(username)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Find user Profile with id
     *
     * @param userId is id of user
     * @return ResponseMessage wrapped in UserProfileDTO
     */
    @GetMapping("find/user/profile/id")
    public ResponseEntity<Object> findUserProfileByUserId(@RequestParam("uid") UUID userId)
    {
        return subscribe(() -> ok(m_service.findUserProfileByUserId(userId)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Find user with profile with given id
     *
     * @param userId is id of user
     * @return UserWithProfileDTO wrapped in MessageResponseDTO
     */
    @GetMapping("find/user-with-profile/id")
    public ResponseEntity<Object> findUserWithProfileByUserId(@RequestParam("uid") UUID userId)
    {
        return subscribe(() -> ok(m_service.findUserWithProfile(userId)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }
}