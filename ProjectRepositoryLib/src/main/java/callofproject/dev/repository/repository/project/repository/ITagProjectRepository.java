package callofproject.dev.repository.repository.project.repository;

import callofproject.dev.repository.repository.project.entity.nosql.ProjectTag;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
public interface ITagProjectRepository extends MongoRepository<ProjectTag, Long>
{
}
