package callofproject.dev.project.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.nosql.dal.ProjectTagServiceHelper;
import callofproject.dev.project.dto.ProjectsParticipantDTO;
import callofproject.dev.project.dto.detail.ProjectDetailDTO;
import callofproject.dev.project.mapper.IProjectMapper;
import callofproject.dev.project.mapper.IProjectParticipantMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.util.stream.StreamUtil.*;
import static java.lang.String.format;

/**
 * Service class for admin-related operations.
 * It implements the IAdminService interface.
 */
@Service
@Lazy
public class AdminService implements IAdminService
{
    private final ProjectServiceHelper m_projectServiceHelper;
    private final ProjectTagServiceHelper m_tagServiceHelper;
    private final ProjectTagServiceHelper m_projectTagServiceHelper;
    private final IProjectParticipantMapper m_participantMapper;
    private final IProjectMapper m_projectMapper;

    /**
     * Constructor for the AdminService class.
     * It is used to inject dependencies into the service.
     *
     * @param projectServiceHelper    The ProjectServiceHelper object to be injected.
     * @param tagServiceHelper        The ProjectTagServiceHelper object to be injected.
     * @param projectTagServiceHelper The ProjectTagServiceHelper object to be injected.
     * @param participantMapper       The IProjectParticipantMapper object to be injected.
     * @param projectMapper           The IProjectMapper object to be injected.
     */
    public AdminService(ProjectServiceHelper projectServiceHelper, ProjectTagServiceHelper tagServiceHelper, ProjectTagServiceHelper projectTagServiceHelper, IProjectParticipantMapper participantMapper, IProjectMapper projectMapper)
    {
        m_projectServiceHelper = projectServiceHelper;
        m_tagServiceHelper = tagServiceHelper;
        m_projectTagServiceHelper = projectTagServiceHelper;
        m_participantMapper = participantMapper;
        m_projectMapper = projectMapper;
    }

    /**
     * Cancels a project given its unique identifier.
     * This method is responsible for initiating the process to cancel a project.
     *
     * @param projectId The UUID of the project to be cancelled.
     * @return A ResponseMessage containing an object, usually providing information about the operation's success or failure.
     */
    @Override
    public ResponseMessage<Object> cancelProject(UUID projectId)
    {
        return doForDataService(() -> cancelProjectCallback(projectId), "Project is canceled!");
    }

    /**
     * Callback method for after a project cancellation is processed.
     * This method might be used for operations that need to be performed after a project has been successfully cancelled.
     *
     * @param projectId The UUID of the cancelled project.
     * @return A ResponseMessage containing an object, typically details or status of the post-cancellation process.
     */
    @Override
    public ResponseMessage<Object> cancelProjectCallback(UUID projectId)
    {
        var project = findProjectIfExistsByProjectId(projectId);
        project.blockProject();
        m_projectServiceHelper.saveProject(project);
        var tags = toStream(m_tagServiceHelper.getAllProjectTagByProjectId(projectId)).toList();
        return new ResponseMessage<>("Project is canceled!", Status.OK, m_projectMapper.toProjectOverviewDTO(project, tags));
    }


    /**
     * Retrieves a paginated list of all projects.
     * This method is used for fetching projects in a paginated format, useful for admin dashboard listing or similar use cases.
     *
     * @param page The page number for which the data is to be fetched.
     * @return A MultipleResponseMessagePageable containing a list of objects (projects) with pagination information.
     */
    @Override
    public MultipleResponseMessagePageable<Object> findAll(int page)
    {
        return doForDataService(() -> findAllCallback(page), "ProjectService::findAll");
    }


    //------------------------------------------------------------------------------------------------------------------
    //#################################################-HELPER METHODS-#################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Callback method for finding all projects with additional details in a paginated format.
     * It retrieves a page of projects, converts them to DTOs, and aggregates additional data like tags and participants.
     *
     * @param page The page number for which the project data is fetched.
     * @return A MultipleResponseMessagePageable object containing a list of project details, total pages, and a status message.
     */
    private MultipleResponseMessagePageable<Object> findAllCallback(int page)
    {
        var projectPageable = m_projectServiceHelper.findAllProjectsPageable(page);
        var projects = toStreamConcurrent(projectPageable).toList();
        var totalPage = projectPageable.getTotalPages();

        if (projects.isEmpty())
            return new MultipleResponseMessagePageable<>(totalPage, page, 0, "Projects not found!", null);

        var projectDetails = new ArrayList<ProjectDetailDTO>();

        for (var project : projects)
        {
            var tags = toStreamConcurrent(m_projectTagServiceHelper.getAllProjectTagByProjectId(project.getProjectId())).toList();
            projectDetails.add(m_projectMapper.toProjectDetailDTO(project, tags, findProjectParticipantsByProjectId(project)));
        }

        var projectWithParticipants = doForDataService(() -> m_projectMapper.toProjectsDetailDTO(projectDetails), "ProjectService::findAllParticipantProjectByUserId");

        return new MultipleResponseMessagePageable<>(totalPage, page, projectDetails.size(), "Projects found!", projectWithParticipants);
    }

    /**
     * Finds and aggregates participants of a given project.
     * It retrieves all participants for a specific project and converts them to a ProjectsParticipantDTO.
     *
     * @param obj The Project entity for which participants are being searched.
     * @return A ProjectsParticipantDTO containing the details of all participants associated with the project.
     */
    private ProjectsParticipantDTO findProjectParticipantsByProjectId(Project obj)
    {
        var participants = m_projectServiceHelper.findAllProjectParticipantByProjectId(obj.getProjectId());
        return m_participantMapper.toProjectsParticipantDTO(toList(participants, m_participantMapper::toProjectParticipantDTO));
    }

    /**
     * Retrieves a project by its unique identifier, if it exists.
     * Throws a DataServiceException if the project is not found.
     *
     * @param projectId The UUID of the project to be fetched.
     * @return The Project entity if it is found.
     * @throws DataServiceException if the project with the specified ID does not exist.
     */
    private Project findProjectIfExistsByProjectId(UUID projectId)
    {
        var project = m_projectServiceHelper.findProjectById(projectId);

        if (project.isEmpty())
            throw new DataServiceException(format("Project with id: %s is not found!", projectId));

        return project.get();
    }
}
