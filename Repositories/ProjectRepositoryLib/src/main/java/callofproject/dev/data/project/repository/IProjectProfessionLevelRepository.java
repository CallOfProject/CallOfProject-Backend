package callofproject.dev.data.project.repository;

import callofproject.dev.data.project.entity.ProjectProfessionLevel;
import callofproject.dev.data.project.entity.enums.EProjectProfessionLevel;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.PROJECT_PROFESSION_LEVEL_REPOSITORY;

@Repository(PROJECT_PROFESSION_LEVEL_REPOSITORY)
@Lazy
public interface IProjectProfessionLevelRepository extends CrudRepository<ProjectProfessionLevel, Long>
{
    @Query("SELECT ppl FROM ProjectProfessionLevel ppl WHERE ppl.m_projectProfessionLevel = :projectProfessionLevel")
    Optional<ProjectProfessionLevel> findProjectProfessionLevelByProjectProfessionLevel(EProjectProfessionLevel projectProfessionLevel);
}
