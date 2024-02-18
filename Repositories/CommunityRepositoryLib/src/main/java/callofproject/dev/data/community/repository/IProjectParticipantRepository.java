package callofproject.dev.data.community.repository;

import callofproject.dev.data.community.entity.ProjectParticipant;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Lazy
public interface IProjectParticipantRepository extends CrudRepository<ProjectParticipant, UUID>
{
}
