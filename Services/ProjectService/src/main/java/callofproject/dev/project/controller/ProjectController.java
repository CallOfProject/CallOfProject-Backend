package callofproject.dev.project.controller;

import callofproject.dev.library.exception.util.CopDataUtil;
import callofproject.dev.project.dto.ParticipantRateDTO;
import callofproject.dev.project.dto.ProjectRateDTO;
import callofproject.dev.project.dto.ProjectSaveDTO;
import callofproject.dev.project.dto.ProjectUpdateDTO;
import callofproject.dev.project.service.IProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

/**
 * This class represents a controller for managing project-related operations.
 * It handles HTTP requests related to projects and interacts with the ProjectService.
 */
@RestController
@RequestMapping("api/project/project")
@SecurityRequirement(name = "Authorization")
public class ProjectController
{
    private final IProjectService m_projectService;
    private final ObjectMapper m_objectMapper;

    /**
     * Constructs a new ProjectController with the provided dependencies.
     *
     * @param projectService The IProjectService instance used for handling project-related operations.
     * @param objectMapper   The ObjectMapper instance used for JSON serialization and deserialization.
     */
    public ProjectController(IProjectService projectService, ObjectMapper objectMapper)
    {
        m_projectService = projectService;
        m_objectMapper = objectMapper;
    }

    /**
     * Handles the HTTP POST request to save a new project.
     *
     * @param saveDTO The ProjectSaveDTO containing project details to be saved.
     * @return ResponseEntity with the result of the save operation as ProjectDTO, or an error message in case of failure.
     */
    @PostMapping("/create")
    public ResponseEntity<Object> save(@RequestBody @Valid ProjectSaveDTO saveDTO)
    {
        return subscribe(() -> ok(m_projectService.saveProject(saveDTO)), msg -> internalServerError().body(msg.getMessage()));
    }

    /**
     * Handles the HTTP POST request to save a new project using a different version (V2) with a file attachment.
     *
     * @param projectSaveDTOJson The JSON representation of the ProjectSaveDTO containing project details.
     * @param file               The file attachment, if any.
     * @return ResponseEntity with the result of the save operation as ProjectDTO, or an error message in case of failure.
     */
    @PostMapping("/create/v2")
    public ResponseEntity<Object> saveV2(@RequestParam("projectSaveDTO") String projectSaveDTOJson, @RequestParam(value = "file") MultipartFile file)
    {
        var dto = CopDataUtil.doForDataService(() -> m_objectMapper.readValue(projectSaveDTOJson, ProjectSaveDTO.class), "ProjectSaveDTO");
        return subscribe(() -> ok(m_projectService.saveProjectV2(dto, file)), msg -> internalServerError().body(msg.getMessage()));
    }

    /**
     * Handles the HTTP PUT request to update an existing project.
     *
     * @param dto The ProjectUpdateDTO containing project details to be updated.
     * @return ResponseEntity with the result of the update operation as ProjectDTO, or an error message in case of failure.
     */
    @PutMapping("/update")
    public ResponseEntity<Object> updateProject(@RequestBody @Valid ProjectUpdateDTO dto)
    {
        return subscribe(() -> ok(m_projectService.updateProject(dto)), msg -> internalServerError().body(msg.getMessage()));
    }


    /**
     * Handles the HTTP POST request to update an existing project using a different version (V2) with a file attachment.
     *
     * @param projectUpdateDTO The JSON representation of the ProjectUpdateDTO containing updated project details.
     * @param file             The file attachment, if any.
     * @return ResponseEntity with the result of the update operation as ProjectDTO, or an error message in case of failure.
     */

    @PostMapping("/update/v2")
    public ResponseEntity<Object> updateProjectV2(@RequestParam("projectUpdateDTO") String projectUpdateDTO, @RequestParam(value = "file", required = false) MultipartFile file)
    {
        var dto = CopDataUtil.doForDataService(() -> m_objectMapper.readValue(projectUpdateDTO, ProjectUpdateDTO.class), "projectUpdateDTO");

        return subscribe(() -> ok(m_projectService.updateProjectV2(dto, file)), msg -> internalServerError().body(msg.getMessage()));
    }

    /**
     * Handles the HTTP POST request to find all projects in which a user is a participant, paginated by page number.
     *
     * @param userId The unique identifier (UUID) of the user.
     * @param page   The page number for paginated results.
     * @return ResponseEntity containing ProjectDTO if successful, or an error message in case of failure.
     */
    @PostMapping("/participant/user-id")
    public ResponseEntity<Object> findAllParticipantProjectByUserId(@RequestParam("uid") String userId, @RequestParam("p") int page)
    {
        return subscribe(() -> ok(m_projectService.findAllParticipantProjectByUserId(UUID.fromString(userId), page)),
                msg -> internalServerError().body(msg.getMessage()));
    }


    /**
     * Handles the HTTP GET request to find all projects owned by a user, paginated by page number.
     *
     * @param userId The unique identifier (UUID) of the user.
     * @param page   The page number for paginated results.
     * @return ResponseEntity containing ProjectDTO if successful, or an error message in case of failure.
     */
    @GetMapping("find/all/owner-id")
    public ResponseEntity<Object> findAllOwnerProjectsByUserId(@RequestParam("uid") UUID userId, @RequestParam("p") int page)
    {
        return subscribe(() -> ok(m_projectService.findAllOwnerProjectsByUserId(userId, page)),
                msg -> internalServerError().body(msg.getMessage()));
    }

    /**
     * Handles the HTTP GET request to find all projects owned by a user with the given username, paginated by page number.
     *
     * @param username The username of the user.
     * @param page     The page number for paginated results.
     * @return ResponseEntity containing ProjectDTO if successful, or an error message in case of failure.
     */
    @GetMapping("find/all/owner-username")
    public ResponseEntity<Object> findAllOwnerProjectsByUsername(String username, int page)
    {
        return subscribe(() -> ok(m_projectService.findAllOwnerProjectsByUsername(username, page)),
                msg -> internalServerError().body(msg.getMessage()));
    }

    /**
     * Handles the HTTP GET request to find an overview of a project by its unique identifier (UUID).
     *
     * @param projectId The unique identifier (UUID) of the project.
     * @return ResponseEntity containing ProjectDTO if successful, or an error message in case of failure.
     */
    @GetMapping("find/overview")
    public ResponseEntity<Object> findProjectOverview(@RequestParam("pid") UUID projectId)
    {
        return subscribe(() -> ok(m_projectService.findProjectOverview(projectId)),
                msg -> internalServerError().body(msg.getMessage()));
    }

    /**
     * Handles the HTTP GET request to find a project from the owner's perspective by user and project IDs.
     *
     * @param userId    The unique identifier (UUID) of the user.
     * @param projectId The unique identifier (UUID) of the project.
     * @return ResponseEntity containing ProjectDetailDTO if successful, or an error message in case of failure.
     */
    @GetMapping("find/detail/owner")
    public ResponseEntity<Object> findProjectOwnerView(@RequestParam("uid") UUID userId, @RequestParam("pid") UUID projectId)
    {
        return subscribe(() -> ok(m_projectService.findProjectOwnerView(userId, projectId)),
                msg -> internalServerError().body(msg.getMessage()));
    }

    /**
     * Handles the HTTP GET request to find all projects in a discovery view, paginated by page number.
     *
     * @param page The page number for paginated results.
     * @return ResponseEntity containing ProjectsDiscoveryDTO if successful, or an error message in case of failure.
     */
    @GetMapping("discovery/all")
    public ResponseEntity<Object> findAllProjectDiscoveryView(@RequestParam("p") int page)
    {
        return subscribe(() -> ok(m_projectService.findAllProjectDiscoveryView(page)), msg -> internalServerError().body(msg.getMessage()));
    }

    /**
     * Handles the HTTP POST request to send a project participant request.
     *
     * @param projectId The unique identifier (UUID) of the project.
     * @param userId    The unique identifier (UUID) of the user sending the request.
     * @return ResponseEntity containing ProjectDTO if successful, or an error message in case of failure.
     */
    @PostMapping("/participant/request")
    public ResponseEntity<Object> addProjectJoinRequest(@RequestParam("pid") UUID projectId, @RequestParam("uid") UUID userId)
    {
        return subscribe(() -> ok(m_projectService.addProjectJoinRequest(projectId, userId)),
                msg -> internalServerError().body(msg.getMessage()));
    }

    /**
     * Handles the HTTP GET request to retrieve detailed information about a specific project.
     *
     * @param projectId The unique identifier (UUID) of the project for which details are requested.
     * @return ResponseEntity containing detailed project information if successful, or an error message in case of failure.
     */
    @GetMapping("/find/detail")
    public ResponseEntity<Object> findProjectDetail(@RequestParam("pid") UUID projectId)
    {
        return subscribe(() -> ok(m_projectService.findProjectDetail(projectId)),
                msg -> internalServerError().body(msg.getMessage()));
    }

    /**
     * Handles the HTTP POST request to rate a project.
     *
     * @param projectRateDTO The ProjectRateDTO containing the project's rating details.
     * @return ResponseEntity containing the result of the rating operation if successful, or an error message in case of failure.
     */
    @PostMapping("/rate")
    public ResponseEntity<Object> rateProject(@RequestBody ProjectRateDTO projectRateDTO)
    {
        return subscribe(() -> ok(m_projectService.rateProject(projectRateDTO)),
                msg -> internalServerError().body(msg.getMessage()));
    }


    /**
     * Handles the HTTP POST request to rate a project participant.
     *
     * @param dto The ParticipantRateDTO containing the participant's rating details.
     * @return ResponseEntity containing the result of the rating operation if successful, or an error message in case of failure.
     */
    @PostMapping("/rate/participant")
    public ResponseEntity<Object> rateProjectParticipant(@RequestBody ParticipantRateDTO dto)
    {
        return subscribe(() -> ok(m_projectService.rateParticipant(dto)),
                msg -> internalServerError().body(msg.getMessage()));
    }

    /**
     * Handles the HTTP GET request to find detailed information about a specific project if the user has permission.
     *
     * @param projectId The unique identifier (UUID) of the project for which details are requested.
     * @param userId    The unique identifier (UUID) of the user making the request.
     * @return ResponseEntity containing detailed project information if the user has permission, or an error message in case of failure or lack of permission.
     */
    @GetMapping("/find/project-detail")
    public ResponseEntity<Object> findProjectDetailIfHasPermission(@RequestParam("pid") UUID projectId, @RequestParam("uid") UUID userId)
    {
        return subscribe(() -> ok(m_projectService.findProjectDetailIfHasPermission(projectId, userId)),
                msg -> internalServerError().body(msg.getMessage()));
    }
}