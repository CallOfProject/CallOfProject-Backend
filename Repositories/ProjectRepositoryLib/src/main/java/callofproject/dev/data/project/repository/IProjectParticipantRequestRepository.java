package callofproject.dev.data.project.repository;

import callofproject.dev.data.project.entity.ProjectParticipantRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.PROJECT_PARTICIPANT_REQUEST_REPOSITORY;

@Repository(PROJECT_PARTICIPANT_REQUEST_REPOSITORY)
@Lazy
public interface IProjectParticipantRequestRepository extends CrudRepository<ProjectParticipantRequest, UUID>
{
    @Query("from ProjectParticipantRequest where m_participantRequestId = :projectParticipantRequestId")
    Optional<ProjectParticipantRequest> findProjectParticipantRequestByParticipantRequestId(UUID projectParticipantRequestId);

    @Query("from ProjectParticipantRequest where m_project.m_projectId = :projectId")
    List<ProjectParticipantRequest> findAllByProjectId(UUID projectId);
}
