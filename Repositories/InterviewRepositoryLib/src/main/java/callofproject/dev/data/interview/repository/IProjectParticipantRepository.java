package callofproject.dev.data.interview.repository;


import callofproject.dev.data.interview.entity.ProjectParticipant;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository("callofproject.dev.data.interview.repository.IProjectParticipantRepository")
@Lazy
public interface IProjectParticipantRepository extends CrudRepository<ProjectParticipant, UUID>
{
    @Query("from ProjectParticipant where m_user.m_userId = :userId and m_project.m_projectId = :projectId")
    Optional<ProjectParticipant> findProjectParticipantByUserIdAndProjectId(UUID userId, UUID projectId);
}
