package callofproject.dev.usermanagement.service;

import callofproject.dev.repository.usermanagement.dal.UserManagementServiceHelper;
import org.springframework.stereotype.Service;

@Service
public class UserManagementService
{
    private final UserManagementServiceHelper m_serviceHelper;

    public UserManagementService(UserManagementServiceHelper serviceHelper)
    {
        m_serviceHelper = serviceHelper;
    }
}
