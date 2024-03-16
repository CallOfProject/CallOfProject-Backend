package callofproject.dev.authentication;

import callofproject.dev.authentication.service.RootService;
import callofproject.dev.authentication.service.admin.AdminServiceCallback;
import callofproject.dev.authentication.service.authentication.AuthenticationServiceCallback;
import callofproject.dev.authentication.service.userinformation.UserInformationServiceCallback;
import callofproject.dev.authentication.service.usermanagement.UserManagementServiceCallback;
import callofproject.dev.repository.authentication.repository.rdbms.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

@Component
@SpringBootTest
public class Injection
{
    @Autowired
    private UserManagementServiceCallback ms_userManagementService;

    @Autowired
    private AuthenticationServiceCallback ms_authenticationService;

    @Autowired
    private AdminServiceCallback m_adminService;

    @Autowired
    private UserInformationServiceCallback m_userInformationService;

    @Autowired
    private RootService m_rootService;

    @Autowired
    private IUserRepository m_userRepository;

    public UserInformationServiceCallback getUserInformationService()
    {
        return m_userInformationService;
    }

    public AdminServiceCallback getAdminService()
    {
        return m_adminService;
    }

    public UserManagementServiceCallback getUserManagementService()
    {
        return ms_userManagementService;
    }

    public AuthenticationServiceCallback getAuthenticationService()
    {
        return ms_authenticationService;
    }

    public IUserRepository getUserRepository()
    {
        return m_userRepository;
    }

    public RootService getRootService()
    {
        return m_rootService;
    }
}
