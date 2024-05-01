package callofproject.dev.repository.authentication.repository.rdbms;

import callofproject.dev.repository.authentication.entity.UserTag;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Lazy
public interface IUserTagRepository extends CrudRepository<UserTag, UUID>
{
    Optional<UserTag> findByTagName(String tagName);

    Optional<UserTag> findByTagId(UUID tagId);
}
