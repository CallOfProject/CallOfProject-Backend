package callofproject.dev.data.project.repository;

import callofproject.dev.data.project.entity.UserProjects;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.USER_PROJECTS_REPOSITORY_BEAN;

@Repository(USER_PROJECTS_REPOSITORY_BEAN)
@Lazy
public interface IUserProjectsRepository extends CrudRepository<UserProjects, Long>
{
    @Query(value = "SELECT * FROM user_projects WHERE user_id = ?1", nativeQuery = true)
    Iterable<UserProjects> findAllByUserId(UUID userId);

    @Query(value = "SELECT * FROM user_projects WHERE project_id = ?1", nativeQuery = true)
    Iterable<UserProjects> findAllByProjectId(UUID projectId);

    @Query(value = "SELECT * FROM user_projects WHERE user_id = ?1 AND project_id = ?2", nativeQuery = true)
    Optional<UserProjects> findByUserIdAndProjectId(UUID userId, UUID projectId);
}
