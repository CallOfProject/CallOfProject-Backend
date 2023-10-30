package callofproject.dev.repository.usermanagement.repository.rdbms;

import callofproject.dev.repository.usermanagement.entity.Experience;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static callofproject.dev.repository.usermanagement.BeanName.EXPERIENCE_REPOSITORY_BEAN;

@Repository(EXPERIENCE_REPOSITORY_BEAN)
@Lazy
public interface IExperienceRepository extends CrudRepository<Experience, UUID>
{
}
