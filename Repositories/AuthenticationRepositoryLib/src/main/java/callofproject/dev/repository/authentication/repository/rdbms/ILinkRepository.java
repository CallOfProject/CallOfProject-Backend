package callofproject.dev.repository.authentication.repository.rdbms;

import callofproject.dev.repository.authentication.entity.Link;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import static callofproject.dev.repository.authentication.BeanName.LINK_REPOSITORY_BEAN;

@Repository(LINK_REPOSITORY_BEAN)
@Lazy
public interface ILinkRepository extends CrudRepository<Link, Long>
{

}
