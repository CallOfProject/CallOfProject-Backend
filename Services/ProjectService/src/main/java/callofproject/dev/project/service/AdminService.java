package callofproject.dev.project.service;

import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.nosql.dal.ProjectTagServiceHelper;
import callofproject.dev.project.mapper.IProjectMapper;
import callofproject.dev.util.stream.StreamUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.util.stream.StreamUtil.toStream;
import static java.lang.String.format;

@Service
@Lazy
public class AdminService
{
    private final ProjectServiceHelper m_projectServiceHelper;
    private final ProjectTagServiceHelper m_tagServiceHelper;
    private final IProjectMapper m_projectMapper;

    public AdminService(ProjectServiceHelper projectServiceHelper, ProjectTagServiceHelper tagServiceHelper, IProjectMapper projectMapper)
    {
        m_projectServiceHelper = projectServiceHelper;
        m_tagServiceHelper = tagServiceHelper;
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

    //------------------------------------------------CALLBACKS---------------------------------------------------------
    public ResponseMessage<Object> cancelProjectCallback(UUID projectId)
    {
        var project = findProjectIfExistsByProjectId(projectId);
        project.blockProject();
        m_projectServiceHelper.saveProject(project);
        var tags = toStream(m_tagServiceHelper.getAllProjectTagByProjectId(projectId)).toList();
        return new ResponseMessage<>("Project is canceled!", Status.OK, m_projectMapper.toProjectOverviewDTO(project, tags));
    }


    //------------------------------------------------HELPER METHODS---------------------------------------------------
    private Project findProjectIfExistsByProjectId(UUID projectId)
    {
        var project = m_projectServiceHelper.findProjectById(projectId);

        if (project.isEmpty())
            throw new DataServiceException(format("Project with id: %s is not found!", projectId));

        return project.get();
    }

    private User findUserIfExists(UUID userId)
    {
        var user = m_projectServiceHelper.findUserById(userId);

        if (user.isEmpty())
            throw new DataServiceException(format("User with id: %s is not found!", userId));

        return user.get();
    }
}
