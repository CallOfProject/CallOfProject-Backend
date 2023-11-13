package callofproject.dev.repository.authentication.dal;

import callofproject.dev.repository.authentication.entity.Role;
import callofproject.dev.repository.authentication.repository.rdbms.IRoleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForRepository;
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
        return doForRepository(() -> m_roleRepository.findRoleByName(roleName), "RoleRepository::findRoleByRoleName");
    }

    public Iterable<Role> findAllRole()
    {
        return doForRepository(m_roleRepository::findAll, "RoleRepository::findAllRole");
    }

    public Role saveRole(Role role)
    {
        return doForRepository(() -> m_roleRepository.save(role), "RoleRepository::saveRole");
    }

    public void saveUserRole(UUID userId, long roleId)
    {
        doForRepository(() -> m_roleRepository.saveUserRole(userId, roleId), "RoleRepository::saveUserRole");
    }
}