package callofproject.dev.data.community.repository;


import callofproject.dev.data.community.entity.Project;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Lazy
public interface IProjectRepository extends JpaRepository<Project, UUID>
{
    @Query("from Project where m_projectId = :projectId")
    Optional<Project> findProjectById(UUID projectId);
}
