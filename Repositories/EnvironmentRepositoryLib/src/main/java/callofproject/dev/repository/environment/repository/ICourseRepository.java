package callofproject.dev.repository.environment.repository;

import callofproject.dev.repository.environment.entity.Course;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static callofproject.dev.repository.environment.BeanName.COURSE_REPOSITORY;

@Repository(COURSE_REPOSITORY)
@Lazy
public interface ICourseRepository extends MongoRepository<Course, String>
{
    Optional<Course> findByCourseNameIgnoreCase(String courseName);

    Iterable<Course> findAllByCourseNameContainsIgnoreCase(String name);
}
