package callofproject.dev.repository.usermanagement.repository.rdbms;

import callofproject.dev.repository.usermanagement.entity.Link;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import static callofproject.dev.repository.usermanagement.BeanName.LINK_REPOSITORY_BEAN;

@Repository(LINK_REPOSITORY_BEAN)
@Lazy
public interface ILinkRepository extends CrudRepository<Link, Long>
{
}
