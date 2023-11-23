package callofproject.dev.data.project.repository;

import callofproject.dev.data.project.entity.ProjectLevel;
import callofproject.dev.data.project.entity.enums.EProjectLevel;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.PROJECT_LEVEL_REPOSITORY;

@Repository(PROJECT_LEVEL_REPOSITORY)
@Lazy
public interface IProjectLevelRepository extends CrudRepository<ProjectLevel, Long>
{
    @Query("SELECT pl FROM ProjectLevel pl WHERE pl.m_projectLevel = :projectLevel")
    Optional<ProjectLevel> findProjectLevelByProjectLevel(EProjectLevel projectLevel);
}
