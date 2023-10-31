package callofproject.dev.repository.usermanagement.repository.rdbms;

import callofproject.dev.repository.usermanagement.entity.University;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static callofproject.dev.repository.usermanagement.BeanName.UNIVERSITY_REPOSITORY_BEAN;

@Repository(UNIVERSITY_REPOSITORY_BEAN)
@Lazy
public interface IUniversityRepository extends CrudRepository<University, Long>
{
    Optional<University> findByUniversityName(String universityName);
}
