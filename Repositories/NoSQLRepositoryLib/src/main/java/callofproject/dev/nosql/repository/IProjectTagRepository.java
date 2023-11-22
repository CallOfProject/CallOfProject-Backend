/*----------------------------------------------------------------
	FILE		: IProjectTagRepository.java
	AUTHOR		: Nuri Can OZTURK
	LAST UPDATE	: 22.11.2023
	IProjectTagRepository interface represent the repository layer of the ProjectTag entity.
	Copyleft (c) NoSQLRepository.
	All Rights Free
----------------------------------------------------------------*/
package callofproject.dev.nosql.repository;

import callofproject.dev.nosql.entity.ProjectTag;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Lazy
public interface IProjectTagRepository extends MongoRepository<ProjectTag, Long>
{
    Iterable<ProjectTag> findAllByProjectId(UUID id);

    Iterable<ProjectTag> findAllByTagName(String tagName);

    Iterable<ProjectTag> findAllByProjectIdAndTagName(UUID projectId, String tagName);
}
