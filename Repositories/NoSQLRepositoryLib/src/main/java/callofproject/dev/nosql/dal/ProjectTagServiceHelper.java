/*----------------------------------------------------------------
	FILE		: ProjectTagServiceHelper.java
	AUTHOR		: Nuri Can OZTURK
	LAST UPDATE	: 22.11.2023
	ProjectTagServiceHelper class represent the helper class of the ProjectTagService.
	Copyleft (c) NoSQLRepository.
	All Rights Free
----------------------------------------------------------------*/
package callofproject.dev.nosql.dal;

import callofproject.dev.nosql.entity.ProjectTag;
import callofproject.dev.nosql.repository.IProjectTagRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForRepository;
import static callofproject.dev.nosql.NoSqlBeanName.PROJECT_TAG_REPOSITORY_BEAN_NAME;
import static callofproject.dev.nosql.NoSqlBeanName.PROJECT_TAG_SERVICE_HELPER_BEAN_NAME;

/**
 * ProjectTagServiceHelper class represent the helper class of the ProjectTagService.
 * Copyleft (c) NoSQLRepository.
 * All Rights Free
 */
@Component(PROJECT_TAG_SERVICE_HELPER_BEAN_NAME)
@Lazy
@SuppressWarnings("all")
public class ProjectTagServiceHelper
{
    private final IProjectTagRepository m_tagRepository;

    /**
     * Constructor
     *
     * @param tagRepository project tag repository
     */
    public ProjectTagServiceHelper(@Qualifier(PROJECT_TAG_REPOSITORY_BEAN_NAME) IProjectTagRepository tagRepository)
    {
        m_tagRepository = tagRepository;
    }

    /**
     * Save Project Tag
     *
     * @param projectTag project tag
     * @return ProjectTag object
     */
    public ProjectTag saveProjectTag(ProjectTag projectTag)
    {
        return doForRepository(() -> m_tagRepository.save(projectTag), "ProjectTagServiceHelper::saveProjectTag");
    }

    /**
     * Remove project tag
     *
     * @param projectTag project tag
     */
    public void removeProjectTag(ProjectTag projectTag)
    {
        doForRepository(() -> m_tagRepository.delete(projectTag), "ProjectTagServiceHelper::removeProjectTag");
    }

    /**
     * Remove project tag by id
     *
     * @param id user id
     */
    public void removeProjectTagById(Long id)
    {
        doForRepository(() -> m_tagRepository.deleteById(id), "ProjectTagServiceHelper::removeProjectTagById");
    }

    /**
     * Find project tag count
     *
     * @return long (count of project tag)
     */
    public long count()
    {
        return doForRepository(() -> m_tagRepository.count(), "ProjectTagServiceHelper::count");
    }

    /**
     * Save all project tag
     *
     * @param projectTags project tag list.
     * @return Iterable project tag
     */
    public Iterable<ProjectTag> saveAll(Iterable<ProjectTag> projectTags)
    {
        return doForRepository(() -> m_tagRepository.saveAll(projectTags), "ProjectTagServiceHelper::saveAll");
    }

    /**
     * Find all tags
     *
     * @return Iterable project tag
     */
    public Iterable<ProjectTag> getAllProjectTag()
    {
        return doForRepository(() -> m_tagRepository.findAll(), "ProjectTagServiceHelper::getAllProjectTag");
    }

    /**
     * Find project tag by id
     *
     * @param projectId project id
     * @return Iterable project tag
     */
    public Iterable<ProjectTag> getAllProjectTagByProjectId(UUID projectId)
    {
        return doForRepository(() -> m_tagRepository.findAllByProjectId(projectId), "ProjectTagServiceHelper::getAllProjectTagByProjectId");
    }

    /**
     * Find project tag by tag name
     *
     * @param tagName tag name
     * @return Iterable project tag
     */
    public Iterable<ProjectTag> getAllProjectTagByTagName(String tagName)
    {
        return doForRepository(() -> m_tagRepository.findAllByTagName(tagName), "ProjectTagServiceHelper::getAllProjectTagByTagName");
    }

    /**
     * Find project tag by project id and tag name
     *
     * @param projectId project id
     * @param tagName   tag name
     * @return Iterable project tag
     */
    public Iterable<ProjectTag> getAllProjectTagByProjectIdAndTagName(UUID projectId, String tagName)
    {
        return doForRepository(() -> m_tagRepository.findAllByProjectIdAndTagName(projectId, tagName), "ProjectTagServiceHelper::getAllProjectTagByProjectIdAndTagName");
    }
}
