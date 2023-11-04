package callofproject.dev.repository.authentication.repository.rdbms;

import callofproject.dev.repository.authentication.entity.Course;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static callofproject.dev.repository.authentication.BeanName.COURSE_REPOSITORY_BEAN;

@Repository(COURSE_REPOSITORY_BEAN)
@Lazy
public interface ICourseRepository extends CrudRepository<Course, UUID>
{
}
