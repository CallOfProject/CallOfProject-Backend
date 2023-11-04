package callofproject.dev.repository.authentication.repository.nosql;

import callofproject.dev.repository.authentication.entity.nosql.MatchDB;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Lazy
public interface IMatchDbRepository extends MongoRepository<MatchDB, UUID>
{
}
