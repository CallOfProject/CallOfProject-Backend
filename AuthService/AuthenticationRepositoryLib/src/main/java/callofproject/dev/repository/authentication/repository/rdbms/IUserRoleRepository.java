package callofproject.dev.repository.authentication.repository.rdbms;

import callofproject.dev.repository.authentication.entity.Role;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import static callofproject.dev.repository.authentication.BeanName.ROLE_REPOSITORY_BEAN;

@Repository(ROLE_REPOSITORY_BEAN)
@Lazy
public interface IUserRoleRepository extends CrudRepository<Role, Long>
{
}
