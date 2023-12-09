package callofproject.dev.project.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.nosql.dal.ProjectTagServiceHelper;
import callofproject.dev.project.dto.overview.ProjectOverviewDTO;
import callofproject.dev.project.mapper.IProjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.util.stream.StreamUtil.toStream;
import static callofproject.dev.util.stream.StreamUtil.toStreamConcurrent;
import static java.lang.String.format;

@Service
@Lazy
public class AdminService
{
    private final ProjectServiceHelper m_projectServiceHelper;
    private final ProjectTagServiceHelper m_tagServiceHelper;
    private final ProjectTagServiceHelper m_projectTagServiceHelper;
    private final IProjectMapper m_projectMapper;

    public AdminService(ProjectServiceHelper projectServiceHelper, ProjectTagServiceHelper tagServiceHelper, ProjectTagServiceHelper projectTagServiceHelper, IProjectMapper projectMapper)
    {
        m_projectServiceHelper = projectServiceHelper;
        m_tagServiceHelper = tagServiceHelper;
        m_projectTagServiceHelper = projectTagServiceHelper;
        m_projectMapper = projectMapper;
    }


    /**
     * Cancel project
     *
     * @param projectId - project id
     * @return if success ProjectDTO else return Error Message
     */
    public ResponseMessage<Object> cancelProject(UUID projectId)
    {
        return doForDataService(() -> cancelProjectCallback(projectId), "Project is canceled!");
    }

    //------------------------------------------------------------------------------------------------------------------
    //####################################################-CALLBACKS-###################################################
    //------------------------------------------------------------------------------------------------------------------
    public ResponseMessage<Object> cancelProjectCallback(UUID projectId)
    {
        var project = findProjectIfExistsByProjectId(projectId);
        project.blockProject();
        m_projectServiceHelper.saveProject(project);
        var tags = toStream(m_tagServiceHelper.getAllProjectTagByProjectId(projectId)).toList();
        return new ResponseMessage<>("Project is canceled!", Status.OK, m_projectMapper.toProjectOverviewDTO(project, tags));
    }


    /**
     * Find all project.
     *
     * @param page represent the page
     * @return ProjectOverviewsDTO class.
     */
    public MultipleResponseMessagePageable<Object> findAll(int page)
    {
        return doForDataService(() -> findAllCallback(page), "ProjectService::findAll");
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################-HELPER METHODS-#################################################
    //------------------------------------------------------------------------------------------------------------------
    private MultipleResponseMessagePageable<Object> findAllCallback(int page)
    {
        var projectPageable = m_projectServiceHelper.findAllProjectsPageable(page);
        var projects = toStreamConcurrent(projectPageable).toList();
        var totalPage = projectPageable.getTotalPages();

        if (projects.isEmpty())
            return new MultipleResponseMessagePageable<>(totalPage, page, 0, "Projects not found!", null);

        var projectOverviewList = new ArrayList<ProjectOverviewDTO>();

        for (var project : projects)
        {
            var tags = toStreamConcurrent(m_projectTagServiceHelper.getAllProjectTagByProjectId(project.getProjectId())).toList();
            projectOverviewList.add(m_projectMapper.toProjectOverviewDTO(project, tags));
        }

        var projectWithParticipants = doForDataService(() -> m_projectMapper.toProjectOverviewsDTO(projectOverviewList), "ProjectService::findAllParticipantProjectByUserId");

        return new MultipleResponseMessagePageable<>(totalPage, page, projectOverviewList.size(), "Projects found!", projectWithParticipants);
    }

    private Project findProjectIfExistsByProjectId(UUID projectId)
    {
        var project = m_projectServiceHelper.findProjectById(projectId);

        if (project.isEmpty())
            throw new DataServiceException(format("Project with id: %s is not found!", projectId));

        return project.get();
    }
}
