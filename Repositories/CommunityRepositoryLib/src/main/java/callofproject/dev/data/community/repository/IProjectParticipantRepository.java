package callofproject.dev.data.community.repository;

import callofproject.dev.data.community.entity.ProjectParticipant;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Lazy
public interface IProjectParticipantRepository extends CrudRepository<ProjectParticipant, UUID>
{
    @Query("from ProjectParticipant as pp where pp.m_project.m_projectId = :projectId and pp.m_user.m_userId = :userId")
    Optional<ProjectParticipant> findProjectParticipantByProjectIdAndUserId(UUID projectId, UUID userId);
}
