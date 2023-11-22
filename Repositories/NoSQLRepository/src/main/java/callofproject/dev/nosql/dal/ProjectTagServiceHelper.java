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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForRepository;

@Component
@Lazy
@SuppressWarnings("all")
public class ProjectTagServiceHelper
{
    private final IProjectTagRepository m_tagRepository;

    public ProjectTagServiceHelper(IProjectTagRepository tagRepository)
    {
        m_tagRepository = tagRepository;
    }

    public ProjectTag saveProjectTag(ProjectTag projectTag)
    {
        return doForRepository(() -> m_tagRepository.save(projectTag), "ProjectTagServiceHelper::saveProjectTag");
    }

    public void removeProjectTag(ProjectTag projectTag)
    {
        doForRepository(() -> m_tagRepository.delete(projectTag), "ProjectTagServiceHelper::removeProjectTag");
    }

    public void removeProjectTagById(Long id)
    {
        doForRepository(() -> m_tagRepository.deleteById(id), "ProjectTagServiceHelper::removeProjectTagById");
    }

    public long count()
    {
        return doForRepository(() -> m_tagRepository.count(), "ProjectTagServiceHelper::count");
    }

    public Iterable<ProjectTag> saveAll(Iterable<ProjectTag> projectTags)
    {
        return doForRepository(() -> m_tagRepository.saveAll(projectTags), "ProjectTagServiceHelper::saveAll");
    }

    public Iterable<ProjectTag> getAllProjectTag()
    {
        return doForRepository(() -> m_tagRepository.findAll(), "ProjectTagServiceHelper::getAllProjectTag");
    }

    public Iterable<ProjectTag> getAllProjectTagByProjectId(UUID projectId)
    {
        return doForRepository(() -> m_tagRepository.findAllByProjectId(projectId), "ProjectTagServiceHelper::getAllProjectTagByProjectId");
    }

    public Iterable<ProjectTag> getAllProjectTagByTagName(String tagName)
    {
        return doForRepository(() -> m_tagRepository.findAllByTagName(tagName), "ProjectTagServiceHelper::getAllProjectTagByTagName");
    }

    public Iterable<ProjectTag> getAllProjectTagByProjectIdAndTagName(UUID projectId, String tagName)
    {
        return doForRepository(() -> m_tagRepository.findAllByProjectIdAndTagName(projectId, tagName), "ProjectTagServiceHelper::getAllProjectTagByProjectIdAndTagName");
    }
}
