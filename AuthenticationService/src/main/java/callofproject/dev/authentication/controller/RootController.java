package callofproject.dev.authentication.controller;

import callofproject.dev.authentication.dto.MessageResponseDTO;
import callofproject.dev.authentication.entity.AuthenticationRequest;
import callofproject.dev.authentication.entity.AuthenticationResponse;
import callofproject.dev.authentication.service.AuthenticationService;
import callofproject.dev.authentication.service.RootService;
import callofproject.dev.authentication.service.UserManagementService;
import callofproject.dev.library.exception.service.DataServiceException;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/root")
public class RootController
{
    private final AuthenticationService m_authenticationService;
    private final UserManagementService m_userManagementService;
    private final RootService m_rootService;

    public RootController(AuthenticationService authenticationService, UserManagementService userManagementService, RootService rootService)
    {
        m_authenticationService = authenticationService;
        m_userManagementService = userManagementService;
        m_rootService = rootService;
    }

    @PostMapping("/give/role/admin")
    public ResponseEntity<MessageResponseDTO<Boolean>> giveAdminRoleToUser(String username)
    {
        try
        {
            return ResponseEntity.ok(m_rootService.giveAdminRoleByUsername(username));
        }
        catch (DataServiceException ex)
        {
            return new ResponseEntity<>(new MessageResponseDTO<Boolean>(ex.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, false),
                    HttpStatusCode.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/remove/role/admin")
    public ResponseEntity<MessageResponseDTO<Boolean>> removeAdminRoleFromUser(String username)
    {
        try
        {
            return ResponseEntity.ok(m_rootService.removeAdminRoleByUsername(username));
        }
        catch (DataServiceException ex)
        {
            return new ResponseEntity<>(new MessageResponseDTO<Boolean>(ex.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, false),
                    HttpStatusCode.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR));
        }
    }
}
