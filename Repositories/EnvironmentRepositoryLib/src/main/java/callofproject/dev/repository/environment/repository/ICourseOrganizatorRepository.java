package callofproject.dev.repository.environment.repository;

import callofproject.dev.repository.environment.entity.CourseOrganization;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static callofproject.dev.repository.environment.BeanName.COURSE_REPOSITORY;

@Repository(COURSE_REPOSITORY)
@Lazy
public interface ICourseOrganizatorRepository extends MongoRepository<CourseOrganization, String>
{
    Optional<CourseOrganization> findByCourseOrganizationNameIgnoreCase(String organizatorName);
    Iterable<CourseOrganization> findAllByCourseOrganizationNameContainsIgnoreCase(String name);
}
