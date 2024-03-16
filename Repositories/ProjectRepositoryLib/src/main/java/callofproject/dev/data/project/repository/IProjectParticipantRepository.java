package callofproject.dev.data.project.repository;

import callofproject.dev.data.project.entity.ProjectParticipant;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.PROJECT_PARTICIPANT_REPOSITORY_BEAN;

@Repository(PROJECT_PARTICIPANT_REPOSITORY_BEAN)
@Lazy
public interface IProjectParticipantRepository extends CrudRepository<ProjectParticipant, UUID>
{
    @Query("from ProjectParticipant where m_project.m_projectId = :projectId")
    Iterable<ProjectParticipant> findAllByProjectId(UUID projectId);

    @Query("from ProjectParticipant where m_user.m_userId = :userId and m_project.m_projectId = :projectId")
    Optional<ProjectParticipant> findProjectParticipantByUserIdAndProjectId(UUID userId, UUID projectId);
}
