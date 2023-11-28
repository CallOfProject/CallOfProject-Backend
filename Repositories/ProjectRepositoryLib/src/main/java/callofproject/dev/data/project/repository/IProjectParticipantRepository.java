package callofproject.dev.data.project.repository;

import callofproject.dev.data.project.entity.ProjectParticipant;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.PROJECT_PARTICIPANT_REPOSITORY_BEAN;

@Repository(PROJECT_PARTICIPANT_REPOSITORY_BEAN)
@Lazy
public interface IProjectParticipantRepository extends CrudRepository<ProjectParticipant, UUID>
{
}
