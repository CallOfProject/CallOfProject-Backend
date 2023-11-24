package callofproject.dev.data.project.repository;

import callofproject.dev.data.project.entity.ProjectParticipants;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.PROJECT_PARTICIPANTS_REPOSITORY_BEAN;

@Repository(PROJECT_PARTICIPANTS_REPOSITORY_BEAN)
@Lazy
public interface IProjectParticipantsRepository extends CrudRepository<ProjectParticipants, Long>
{
}
