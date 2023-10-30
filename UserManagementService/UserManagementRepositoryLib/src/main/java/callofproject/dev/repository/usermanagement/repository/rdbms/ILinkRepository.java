package callofproject.dev.repository.usermanagement.repository.rdbms;

import callofproject.dev.repository.usermanagement.entity.Link;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
public interface ILinkRepository extends CrudRepository<Link, Long>
{
}
