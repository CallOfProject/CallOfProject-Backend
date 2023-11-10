package callofproject.dev.authentication.controller;

import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.repository.authentication.dto.MessageResponseDTO;
import callofproject.dev.repository.authentication.dto.UserSignUpRequestDTO;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.authentication.service.UserManagementService;
import jakarta.annotation.security.RolesAllowed;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/users")
@RestController
public class UserManagementController
{
    private final UserManagementService m_service;

    public UserManagementController(UserManagementService service)
    {
        m_service = service;
    }

    @PostMapping("save/user")
    public ResponseEntity<MessageResponseDTO<User>> saveUser(@RequestBody UserSignUpRequestDTO dto)
    {
        try
        {
            var user = m_service.saveUser(dto);
            var message = "User inserted successfully!";
            var msgResponse = new MessageResponseDTO<User>(message, HttpStatus.SC_OK, user.getObject());
            return ResponseEntity.ok(msgResponse);

        }
        catch (DataServiceException ignored)
        {
            var msgResponse = new MessageResponseDTO<User>(ignored.getMessage(), 505, null);
            return new ResponseEntity<>(msgResponse, HttpStatusCode.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR));
        }
    }


    @GetMapping("find/user/username")
    public ResponseEntity<MessageResponseDTO<User>> findUserByUsername(@RequestParam("n") String username)
    {
        try
        {
            var user = m_service.findUserByUsername(username);
            var message = "User found successfully!";
            var msgResponse = new MessageResponseDTO<User>(message, HttpStatus.SC_OK, user.getObject());
            return ResponseEntity.ok(msgResponse);

        } catch (DataServiceException ignored)
        {
            var msgResponse = new MessageResponseDTO<User>(ignored.getMessage(), 505, null);
            return new ResponseEntity<>(msgResponse, HttpStatusCode.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR));
        }
    }
}
