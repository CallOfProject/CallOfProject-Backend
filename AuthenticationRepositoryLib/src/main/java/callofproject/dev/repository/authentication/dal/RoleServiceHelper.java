package callofproject.dev.repository.authentication.dal;

import callofproject.dev.repository.authentication.entity.Role;
import callofproject.dev.repository.authentication.repository.rdbms.IRoleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.repository.authentication.BeanName.ROLE_DAL_BEAN;
import static callofproject.dev.repository.authentication.BeanName.ROLE_REPOSITORY_BEAN;

@Component(ROLE_DAL_BEAN)
@Lazy
public class RoleServiceHelper
{

    private final IRoleRepository m_roleRepository;

    public RoleServiceHelper(@Qualifier(ROLE_REPOSITORY_BEAN) IRoleRepository roleRepository)
    {
        m_roleRepository = roleRepository;
    }

    public Iterable<Role> findRoleByRoleName(String roleName)
    {
        return m_roleRepository.findRoleByName(roleName);
    }

    public Iterable<Role> findAllRole()
    {
        return m_roleRepository.findAll();
    }

    public Role saveRole(Role role)
    {
        return m_roleRepository.save(role);
    }

    public void saveUserRole(UUID userId, long roleId)
    {
        m_roleRepository.saveUserRole(userId, roleId);
    }
}