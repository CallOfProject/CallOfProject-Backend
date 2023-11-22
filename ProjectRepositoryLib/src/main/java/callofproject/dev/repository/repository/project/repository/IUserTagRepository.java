package callofproject.dev.repository.repository.project.repository;

import callofproject.dev.repository.repository.project.entity.nosql.UserTag;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Lazy
public interface IUserTagRepository extends MongoRepository<UserTag, Long>
{
    Iterable<UserTag> findAllByUserId(UUID id);
}
