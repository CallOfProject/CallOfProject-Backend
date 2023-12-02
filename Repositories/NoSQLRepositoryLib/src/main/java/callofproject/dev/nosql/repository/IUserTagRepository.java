/*----------------------------------------------------------------
	FILE		: IUserTagRepository.java
	AUTHOR		: Nuri Can OZTURK
	LAST UPDATE	: 22.11.2023
	IUserTagRepository interface represent the repository layer of the UserTag entity.
	Copyleft (c) NoSQLRepository.
	All Rights Free
----------------------------------------------------------------*/
package callofproject.dev.nosql.repository;

import callofproject.dev.nosql.entity.ProjectTag;
import callofproject.dev.nosql.entity.UserTag;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static callofproject.dev.nosql.NoSqlBeanName.USER_TAG_REPOSITORY_BEAN_NAME;

/**
 * IUserTagRepository interface represent the repository layer of the UserTag entity.
 * Copyleft (c) NoSQLRepository.
 * All Rights Free
 */
@Repository(USER_TAG_REPOSITORY_BEAN_NAME)
@Lazy
public interface IUserTagRepository extends MongoRepository<UserTag, UUID>
{
    /**
     * Find all user tag by user id
     *
     * @param id user id
     * @return user tag list
     */
    Iterable<UserTag> findAllByUserId(UUID id);

    /**
     * Find all user tag by tag name
     *
     * @param tagName tag name
     * @return user tag list
     */
    Iterable<UserTag> findAllByTagName(String tagName);

    /**
     * Find all user tag by user id and tag name
     *
     * @param userId  user id
     * @param tagName tag name
     * @return user tag list
     */
    Iterable<UserTag> findAllByUserIdAndTagName(UUID userId, String tagName);

}