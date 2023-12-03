package callofproject.dev.repository.authentication.repository.rdbms;

import callofproject.dev.repository.authentication.entity.CourseOrganization;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static callofproject.dev.repository.authentication.BeanName.COURSE_ORGANIZATION_REPOSITORY_BEAN;

@Repository(COURSE_ORGANIZATION_REPOSITORY_BEAN)
@Lazy
public interface ICourseOrganizationRepository extends CrudRepository<CourseOrganization, UUID>
{
}
