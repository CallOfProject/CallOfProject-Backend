package callofproject.dev.authentication.controller;

import callofproject.dev.authentication.dto.UserProfileUpdateDTO;
import callofproject.dev.authentication.dto.UserSignUpRequestDTO;
import callofproject.dev.authentication.dto.UserTagCreateDTO;
import callofproject.dev.authentication.dto.UserTagUpdateDTO;
import callofproject.dev.authentication.service.usermanagement.UserManagementService;
import callofproject.dev.data.common.clas.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
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
    private final ObjectMapper m_objectMapper;

    /**
     * Constructor for the UserManagementController class.
     * It is used to inject dependencies into the controller.
     *
     * @param service The UserManagementService object to be injected.
     */
    public UserManagementController(UserManagementService service, ObjectMapper objectMapper)
    {
        m_service = service;
        m_objectMapper = objectMapper;
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
     * @return UserDTO wrapped in MessageResponseDTO
     */
    @PostMapping(value = "update/user/profile")
    public ResponseEntity<Object> updateUserProfile(@RequestParam("dto") String dto,
                                                    @RequestParam(value = "photo", required = false) MultipartFile photo,
                                                    @RequestParam(value = "cv", required = false) MultipartFile cv)
    {
        var obj = doForDataService(() -> m_objectMapper.readValue(dto, UserProfileUpdateDTO.class), "ProjectSaveDTO");

        return subscribe(() -> ok(m_service.upsertUserProfile(obj, photo, cv)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Find user with given id
     *
     * @return UserDTO wrapped in MessageResponseDTO
     */
    @PutMapping(value = "update/user/profile/about-me")
    public ResponseEntity<Object> updateUserProfileMobile(@RequestParam("user_id") UUID userId,
                                                          @RequestParam(value = "about_me") String aboutMe)
    {
        return subscribe(() -> ok(m_service.updateAboutMe(userId, aboutMe)),
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
    public ResponseEntity<Object> uploadProfilePhoto(@RequestParam("uid") UUID userId, @RequestParam MultipartFile file)
    {
        return subscribe(() -> ok(m_service.uploadUserProfilePhoto(userId, file)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }


    /**
     * Upload user profile photo.
     *
     * @param userId represent the user id
     * @param file   represent the file
     * @return boolean value success or not
     */
    @PostMapping("upload/cv")
    public ResponseEntity<Object> uploadUserCv(@RequestParam("uid") UUID userId, @RequestParam MultipartFile file)
    {
        return subscribe(() -> ok(m_service.uploadUserCv(userId, file)),
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

    @PostMapping("create/user-tag")
    public ResponseEntity<Object> addTagToUser(@RequestBody UserTagCreateDTO dto)
    {
        return subscribe(() -> ok(m_service.createUserTag(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    @PostMapping("update/user-tag")
    public ResponseEntity<Object> updateUserTags(@RequestBody UserTagUpdateDTO dto)
    {
        return subscribe(() -> ok(m_service.updateUserTag(dto)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }


    @DeleteMapping("delete/user-tag")
    public ResponseEntity<Object> deleteUserTag(@RequestParam("uid") UUID userId, @RequestParam("tid") UUID tagId)
    {
        return subscribe(() -> ok(m_service.deleteUserTag(userId, tagId)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }
}