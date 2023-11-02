package callofproject.dev.usermanagement.controller;

import callofproject.dev.usermanagement.service.UserManagementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api")
@RestController
public class UserManagementController
{
    private final UserManagementService m_service;

    public UserManagementController(UserManagementService service)
    {
        m_service = service;
    }

    @GetMapping
    public String hello()
    {
        return "Hello!";
    }
}
