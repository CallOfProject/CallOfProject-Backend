package callofproject.dev.data.interview.repository;


import callofproject.dev.data.interview.entity.ProjectParticipant;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("callofproject.dev.data.interview.repository.IProjectParticipantRepository")
@Lazy
public interface IProjectParticipantRepository extends CrudRepository<ProjectParticipant, UUID>
{
}
