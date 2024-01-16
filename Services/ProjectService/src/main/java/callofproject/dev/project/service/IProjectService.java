package callofproject.dev.project.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.project.dto.ProjectSaveDTO;
import callofproject.dev.project.dto.ProjectUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface IProjectService
{
    ResponseMessage<Object> saveProject(ProjectSaveDTO projectDTO);
    ResponseMessage<Object> saveProjectV2(ProjectSaveDTO saveDTO, MultipartFile file);
    MultipleResponseMessagePageable<Object> findAllParticipantProjectByUserId(UUID userId, int page);
    ResponseMessage<Object> updateProject(ProjectUpdateDTO projectDTO);
    ResponseMessage<Object> updateProjectV2(ProjectUpdateDTO projectDTO, MultipartFile file);
    MultipleResponseMessagePageable<Object> findAllOwnerProjectsByUserId(UUID userId, int page);
    MultipleResponseMessagePageable<Object> findAllOwnerProjectsByUsername(String username, int page);
    ResponseMessage<Object> findProjectOwnerView(UUID userId, UUID projectId);
    MultipleResponseMessagePageable<Object> findAllProjectDiscoveryView(int page);
    ResponseMessage<Object> findProjectOverview(UUID projectId);
    ResponseMessage<Object> addProjectJoinRequest(UUID projectId, UUID userId);
    ResponseMessage<Object> findProjectDetail(UUID projectId);
    ResponseMessage<Object> findProjectDetailIfHasPermission(UUID projectId, UUID userId);
    ResponseMessage<Object> addProjectJoinRequestCallback(UUID projectId, UUID userId);
}
