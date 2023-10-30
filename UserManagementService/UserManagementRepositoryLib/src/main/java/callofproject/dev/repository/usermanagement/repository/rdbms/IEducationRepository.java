package callofproject.dev.repository.usermanagement.repository.rdbms;

import callofproject.dev.repository.usermanagement.entity.Education;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static callofproject.dev.repository.usermanagement.BeanName.EDUCATION_REPOSITORY_BEAN;

@Repository(EDUCATION_REPOSITORY_BEAN)
@Lazy
public interface IEducationRepository extends CrudRepository<Education, UUID>
{
}
