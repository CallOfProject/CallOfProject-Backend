package callofproject.dev.repository.environment.repository;

import callofproject.dev.repository.environment.entity.CourseOrganizator;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static callofproject.dev.repository.environment.BeanName.COURSE_REPOSITORY;

@Repository(COURSE_REPOSITORY)
@Lazy
public interface ICourseOrganizatorRepository extends CrudRepository<CourseOrganizator, Long>
{
    Optional<CourseOrganizator> findByOrganizatorName(String organizatorName);
}
