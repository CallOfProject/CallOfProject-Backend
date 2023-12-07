package callofproject.dev.repository.authentication.repository.rdbms;

import callofproject.dev.repository.authentication.entity.Education;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.repository.authentication.BeanName.EDUCATION_REPOSITORY_BEAN;

@Repository(EDUCATION_REPOSITORY_BEAN)
@Lazy
public interface IEducationRepository extends CrudRepository<Education, UUID>
{
    boolean existsEducationBySchoolNameContainsIgnoreCase(String schoolName);

    Optional<Education> findBySchoolNameContainsIgnoreCase(String schoolName);
}
