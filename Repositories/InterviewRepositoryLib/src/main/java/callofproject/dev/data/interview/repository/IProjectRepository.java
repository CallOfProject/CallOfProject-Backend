package callofproject.dev.data.interview.repository;



import callofproject.dev.data.interview.entity.Project;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("callofproject.dev.data.interview.repository.IProjectRepository")
@Lazy
public interface IProjectRepository extends JpaRepository<Project, UUID>
{
    @Query("FROM Project p WHERE p.m_projectOwner.m_userId = :userId")
    Iterable<Project> findOwnerProjectsByUserId(UUID userId);
}
