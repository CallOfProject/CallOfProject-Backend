package callofproject.dev.project.service;

import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.project.entity.enums.EProjectStatus;
import callofproject.dev.project.dto.ParticipantRequestDTO;
import callofproject.dev.project.dto.SaveProjectParticipantDTO;

import java.util.UUID;

public interface IProjectOwnerService
{
    ResponseMessage<Boolean> addParticipant(SaveProjectParticipantDTO dto);
    ResponseMessage<Object> removeParticipant(UUID projectId, UUID userId);
    ResponseMessage<Object> approveParticipantRequest(ParticipantRequestDTO requestDTO);
    ResponseMessage<Object> finishProject(UUID userId, UUID projectId);
    ResponseMessage<Object> changeProjectStatus(UUID userId, UUID projectId, EProjectStatus projectStatus);
    ResponseMessage<Object> removeProject(UUID userId, UUID projectId);
    ResponseMessage<Object> approveParticipantRequestCallback(ParticipantRequestDTO requestDTO);
    ResponseMessage<Object> removeParticipantCallback(UUID projectId, UUID userId);
}
