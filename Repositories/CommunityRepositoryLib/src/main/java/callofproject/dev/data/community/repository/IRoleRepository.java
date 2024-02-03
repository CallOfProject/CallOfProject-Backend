package callofproject.dev.data.community.repository;


import callofproject.dev.data.community.entity.Role;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
public interface IRoleRepository extends CrudRepository<Role, Long>
{
}
