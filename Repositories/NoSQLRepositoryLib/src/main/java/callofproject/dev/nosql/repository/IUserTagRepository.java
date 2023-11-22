/*----------------------------------------------------------------
	FILE		: IUserTagRepository.java
	AUTHOR		: Nuri Can OZTURK
	LAST UPDATE	: 22.11.2023
	IUserTagRepository interface represent the repository layer of the UserTag entity.
	Copyleft (c) NoSQLRepository.
	All Rights Free
----------------------------------------------------------------*/
package callofproject.dev.nosql.repository;

import callofproject.dev.nosql.entity.UserTag;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Lazy
public interface IUserTagRepository extends MongoRepository<UserTag, Long>
{
    Iterable<UserTag> findAllByUserId(UUID id);

    Iterable<UserTag> findAllByTagName(String tagName);

    Iterable<UserTag> findAllByUserIdAndTagName(UUID userId, String tagName);
}