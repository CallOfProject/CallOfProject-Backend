package callofproject.dev.authentication;

import callofproject.dev.authentication.service.AuthenticationService;
import callofproject.dev.authentication.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

@Component
@SpringBootTest
public class Injection
{
    @Autowired
    private UserManagementService ms_userManagementService;
    @Autowired
    private AuthenticationService ms_authenticationService;

    public UserManagementService getUserManagementService()
    {
        return ms_userManagementService;
    }

    public AuthenticationService getAuthenticationService()
    {
        return ms_authenticationService;
    }
}
