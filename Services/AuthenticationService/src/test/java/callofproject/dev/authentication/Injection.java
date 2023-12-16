package callofproject.dev.authentication;

import callofproject.dev.authentication.controller.AdminController;
import callofproject.dev.authentication.service.*;
import callofproject.dev.repository.authentication.repository.rdbms.IUserRepository;
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

    @Autowired
    private AdminService m_adminService;

    @Autowired
    private UserInformationService m_userInformationService;

    @Autowired
    private RootService m_rootService;

    @Autowired
    private IUserRepository m_userRepository;

    @Autowired
    private AdminController m_adminController;

    public AdminController getAdminController()
    {
        return m_adminController;
    }
    public UserInformationService getUserInformationService()
    {
        return m_userInformationService;
    }

    public AdminService getAdminService()
    {
        return m_adminService;
    }

    public UserManagementService getUserManagementService()
    {
        return ms_userManagementService;
    }

    public AuthenticationService getAuthenticationService()
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
