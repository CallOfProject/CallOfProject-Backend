package callofproject.dev.repository.repository.project.repository;

import callofproject.dev.repository.repository.project.entity.ProjectProfessionLevel;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
public interface IProjectProfessionLevelRepository extends CrudRepository<ProjectProfessionLevel, Long>
{
}
