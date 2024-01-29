package callofproject.dev.data.task.repository;

import callofproject.dev.data.task.entity.Project;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Lazy
public interface IProjectRepository extends JpaRepository<Project, UUID>
{
}
