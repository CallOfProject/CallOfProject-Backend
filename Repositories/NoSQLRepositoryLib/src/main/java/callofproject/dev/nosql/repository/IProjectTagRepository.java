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

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.nosql.NoSqlBeanName.PROJECT_TAG_REPOSITORY_BEAN_NAME;

/**
 * IProjectTagRepository interface represent the repository layer of the ProjectTag entity.
 * Copyleft (c) NoSQLRepository.
 * All Rights Free
 */
@Repository(PROJECT_TAG_REPOSITORY_BEAN_NAME)
@Lazy
public interface IProjectTagRepository extends MongoRepository<ProjectTag, String>
{
    /**
     * Find all project tags by project id
     *
     * @param id project id
     * @return project tags
     */
    Iterable<ProjectTag> findAllByProjectId(UUID id);

    /**
     * Find all project tags by tag name
     *
     * @param tagName tag name
     * @return project tags
     */
    Iterable<ProjectTag> findAllByTagName(String tagName);

    /**
     * Find all project tags by project id and tag name
     *
     * @param projectId project id
     * @param tagName   tag name
     * @return project tags
     */
    Iterable<ProjectTag> findAllByProjectIdAndTagName(UUID projectId, String tagName);

    Optional<ProjectTag> findProjectTagByTagNameContainsIgnoreCase(String tagName);

    boolean existsProjectTagByTagNameContainsIgnoreCase(String tagName);

}
