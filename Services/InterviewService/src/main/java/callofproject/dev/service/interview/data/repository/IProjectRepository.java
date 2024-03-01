package callofproject.dev.service.interview.data.repository;


import callofproject.dev.service.interview.data.entity.Project;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Lazy
public interface IProjectRepository extends JpaRepository<Project, UUID>
{
    @Query("FROM Project p WHERE p.m_projectOwner.m_userId = :userId")
    Iterable<Project> findOwnerProjectsByUserId(UUID userId);
}
