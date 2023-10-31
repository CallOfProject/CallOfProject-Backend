package callofproject.dev.repository.usermanagement.repository.nosql;

import callofproject.dev.repository.usermanagement.entity.nosql.MatchDB;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Lazy
public interface IMatchDbRepository extends MongoRepository<MatchDB, UUID>
{
}
