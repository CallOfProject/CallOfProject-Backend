package callofproject.dev.repository.usermanagement.repository.rdbms;

import callofproject.dev.repository.usermanagement.entity.Course;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static callofproject.dev.repository.usermanagement.BeanName.COURSE_REPOSITORY_BEAN;

@Repository(COURSE_REPOSITORY_BEAN)
@Lazy
public interface ICourseRepository extends CrudRepository<Course, UUID>
{
}
