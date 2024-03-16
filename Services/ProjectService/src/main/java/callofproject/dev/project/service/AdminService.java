package callofproject.dev.project.service;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.nosql.dal.ProjectTagServiceHelper;
import callofproject.dev.project.dto.ProjectAdminDTO;
import callofproject.dev.project.dto.ProjectsParticipantDTO;
import callofproject.dev.project.dto.detail.ProjectDetailDTO;
import callofproject.dev.project.mapper.IProjectMapper;
import callofproject.dev.project.mapper.IProjectParticipantMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
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
    private final S3Service m_storageService;

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
    public AdminService(ProjectServiceHelper projectServiceHelper, ProjectTagServiceHelper tagServiceHelper, ProjectTagServiceHelper projectTagServiceHelper, IProjectParticipantMapper participantMapper, IProjectMapper projectMapper, S3Service storageService)
    {
        m_projectServiceHelper = projectServiceHelper;
        m_tagServiceHelper = tagServiceHelper;
        m_projectTagServiceHelper = projectTagServiceHelper;
        m_participantMapper = participantMapper;
        m_projectMapper = projectMapper;
        m_storageService = storageService;
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

    @Override
    public MultipleResponseMessage<Object> findAllProjects()
    {
        return doForDataService(this::findAllProjectsCallback, "ProjectService::findAll");
    }

    @Override
    public MultipleResponseMessagePageable<Object> findAllProjectsByPage(int page)
    {
        return doForDataService(() -> findAllProjectsByPageCallback(page), "ProjectService::findAll");
    }

    @Override
    public ResponseMessage<Object> updateProject(ProjectAdminDTO dto, MultipartFile file)
    {
        var project = findProjectIfExistsByProjectId(dto.projectId());


        var compressedPhoto = file != null ? compressImageToJPEG(file) : null;

        var profilePhotoUrl = compressedPhoto != null ? uploadProfilePhoto(compressedPhoto, project, dto.projectOwnerUsername()) : null;

        if (profilePhotoUrl != null)
            project.setProjectImagePath(profilePhotoUrl);

        project.setProjectName(dto.projectName());
        project.setProjectSummary(dto.projectSummary());
        project.setDescription(dto.description());
        project.setProjectAim(dto.projectAim());
        project.setProjectStatus(dto.projectStatus());
        project.setProjectAccessType(dto.projectAccessType());
        project.setProfessionLevel(dto.professionLevel());
        project.setDegree(dto.degree());
        project.setProjectLevel(dto.projectLevel());
        project.setStartDate(dto.startDate());
        project.setExpectedCompletionDate(dto.expectedCompletionDate());
        project.setApplicationDeadline(dto.applicationDeadline());
        project.setCompletionDate(dto.completionDate());
        project.setMaxParticipant(dto.maxParticipants());
        m_projectServiceHelper.saveProject(project);

        return new ResponseMessage<>("Project is updated!", Status.OK, profilePhotoUrl);
    }

    @Override
    public ResponseMessage<Object> getTotalProjectCount()
    {
        var projects = toStreamConcurrent(m_projectServiceHelper.findAllProjects()).toList();

        return new ResponseMessage<>("Total project count found!", Status.OK, projects.size());
    }

    private String uploadProfilePhoto(byte[] profilePhoto, Project project, String username)
    {
        var fileName = "pp_" + username + "_" + project.getProjectId() + "_" + System.currentTimeMillis() + ".jpg";
        return m_storageService.uploadToS3WithByteArray(profilePhoto, fileName, Optional.empty());
    }

    public byte[] compressImageToJPEG(MultipartFile file)
    {
        try
        {
            BufferedImage originalImage = ImageIO.read(file.getInputStream());

            // Create a BufferedImage with RGB color space for compatibility with JPEG
            BufferedImage rgbImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            rgbImage.createGraphics().drawImage(originalImage, 0, 0, null);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Create JPEG writer with specified quality
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.5f); // You can adjust the compression quality here

            // Write the compressed image to the output stream
            writer.setOutput(new MemoryCacheImageOutputStream(outputStream));
            writer.write(null, new IIOImage(rgbImage, null, null), param);

            // Encode the output stream to base64 or return byte array
            byte[] compressedBytes = outputStream.toByteArray();

            // Close writer and output stream
            writer.dispose();
            outputStream.close();

            return compressedBytes;
        } catch (IOException e)
        {
            throw new DataServiceException("Error occurred while compressing image!", e);
        }
    }

    private MultipleResponseMessagePageable<Object> findAllProjectsByPageCallback(int page)
    {
        var projectPageable = m_projectServiceHelper.findAllProjectsPageable(page);
        var projects = toStreamConcurrent(projectPageable).toList();
        var totalPage = projectPageable.getTotalPages();

        if (projects.isEmpty())
            return new MultipleResponseMessagePageable<>(totalPage, page, 0, "Projects not found!", null);

        var projectsAdminDTO = projects.stream().map(m_projectMapper::toProjectAdminDTO).toList();

        return new MultipleResponseMessagePageable<>(totalPage, page, projectsAdminDTO.size(), "Projects found!", projectsAdminDTO);
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

    private MultipleResponseMessage<Object> findAllProjectsCallback()
    {
        var projects = toStreamConcurrent(m_projectServiceHelper.findAllProjects()).toList();

        if (projects.isEmpty())
            return new MultipleResponseMessage<>(0, "Projects not found!", null);

        var projectsAdminDTO = projects.stream().map(m_projectMapper::toProjectAdminDTO).toList();

        return new MultipleResponseMessage<>(projectsAdminDTO.size(), "Projects found!", projectsAdminDTO);
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
