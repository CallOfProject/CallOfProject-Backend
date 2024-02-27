package callofproject.dev.service.interview.service.codinginterview;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.service.interview.dto.coding.CreateCodingInterviewDTO;

import java.util.UUID;

public interface ICodingInterviewService
{
    ResponseMessage<Object> createCodeInterview(CreateCodingInterviewDTO dto);

    ResponseMessage<Object> deleteCodeInterview(UUID ownerId, UUID codeInterviewId);

    ResponseMessage<Object> deleteCodeInterviewByProjectId(UUID projectId);

    ResponseMessage<Object> addParticipant(UUID codeInterviewId, UUID userId);

    ResponseMessage<Object> addParticipantByProjectId(UUID projectId, UUID userId);

    ResponseMessage<Object> removeParticipant(UUID codeInterviewId, UUID userId);

    ResponseMessage<Object> removeParticipantByProjectId(UUID projectId, UUID userId);

    MultipleResponseMessage<Object> getParticipants(UUID codeInterviewId);

    MultipleResponseMessage<Object> getParticipantsByProjectId(UUID projectId);

    ResponseMessage<Object> getInterviewByProjectId(UUID projectId);

    ResponseMessage<Object> getInterview(UUID codeInterviewId);

    MultipleResponseMessage<Object> getAllInterviews();
}
