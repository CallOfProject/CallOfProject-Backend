package callofproject.dev.repository.repository.project.repository;

import callofproject.dev.repository.repository.project.entity.ProjectAccessType;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
public interface IProjectAccessTypeRepository extends CrudRepository<ProjectAccessType, Long>
{
}
