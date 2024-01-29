package callofproject.dev.data.task.repository;

import callofproject.dev.data.task.entity.Role;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
public interface IRoleRepository extends CrudRepository<Role, Long>
{
}
