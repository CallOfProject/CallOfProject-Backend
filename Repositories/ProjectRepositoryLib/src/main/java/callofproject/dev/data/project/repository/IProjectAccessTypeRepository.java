package callofproject.dev.data.project.repository;

import callofproject.dev.data.project.entity.ProjectAccessType;
import callofproject.dev.data.project.entity.enums.EProjectAccessType;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.PROJECT_ACCESS_TYPE_REPOSITORY;

@Repository(PROJECT_ACCESS_TYPE_REPOSITORY)
@Lazy
public interface IProjectAccessTypeRepository extends CrudRepository<ProjectAccessType, Long>
{
    @Query("SELECT pat FROM ProjectAccessType pat WHERE pat.m_projectAccessType = :projectAccessType")
    Optional<ProjectAccessType> findProjectAccessTypeByProjectAccessType(EProjectAccessType projectAccessType);
}
