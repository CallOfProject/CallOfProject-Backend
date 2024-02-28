package callofproject.dev.service.interview.data.repository;


import callofproject.dev.service.interview.data.entity.Role;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
public interface IRoleRepository extends CrudRepository<Role, Long>
{
}