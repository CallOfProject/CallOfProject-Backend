package callofproject.dev.authentication.service;

import callofproject.dev.authentication.dto.MessageResponseDTO;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.repository.authentication.dal.UserManagementServiceHelper;
import callofproject.dev.repository.authentication.entity.Role;
import callofproject.dev.repository.authentication.enumeration.RoleEnum;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class RootService
{
    private final UserManagementServiceHelper m_managementServiceHelper;

    public RootService(UserManagementServiceHelper managementServiceHelper)
    {
        m_managementServiceHelper = managementServiceHelper;
    }


    public MessageResponseDTO<Boolean> giveAdminRoleByUsername(String username)
    {
        var user = m_managementServiceHelper.getUserServiceHelper().findByUsername(username);

        if (user.isEmpty())
            throw new DataServiceException("User does not exists!");

        if (user.get().getRoles().stream().map(Role::getAuthority).anyMatch(role -> role.equals(RoleEnum.ROLE_ADMIN.getRole())))
            throw new DataServiceException("User already has admin role!");

        user.get().addRoleToUser(new Role(RoleEnum.ROLE_ADMIN.getRole()));

        m_managementServiceHelper.getUserServiceHelper().saveUser(user.get());

        return new MessageResponseDTO<>("Success!", HttpStatus.SC_OK, true);
    }

    public MessageResponseDTO<Boolean> removeAdminRoleByUsername(String username)
    {
        var user = m_managementServiceHelper.getUserServiceHelper().findByUsername(username);

        if (user.isEmpty())
            throw new DataServiceException("User does not exists!");

        var role = user.get().getRoles().stream().filter(r -> r.getAuthority().equals(RoleEnum.ROLE_ADMIN.getRole())).findFirst();

        if (role.isEmpty())
            throw new DataServiceException("User does not have admin role!");

        user.get().getRoles().remove(role.get());

        m_managementServiceHelper.getUserServiceHelper().saveUser(user.get());

        return new MessageResponseDTO<>("Success!", HttpStatus.SC_OK, true);
    }
}
