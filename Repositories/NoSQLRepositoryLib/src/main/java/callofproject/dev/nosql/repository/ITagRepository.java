package callofproject.dev.nosql.repository;

import callofproject.dev.nosql.entity.Tag;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static callofproject.dev.nosql.NoSqlBeanName.TAG_REPOSITORY_BEAN_NAME;

@Repository(TAG_REPOSITORY_BEAN_NAME)
@Lazy
public interface ITagRepository extends MongoRepository<Tag, String>
{
    boolean existsByTagNameContainsIgnoreCase(String tagName);

    Optional<Tag> findByTagNameContainsIgnoreCase(String tagName);

    default Tag saveTagIfNotExists(String tagName)
    {
        return findByTagNameContainsIgnoreCase(tagName).orElseGet(() -> save(new Tag(tagName)));
    }
}
