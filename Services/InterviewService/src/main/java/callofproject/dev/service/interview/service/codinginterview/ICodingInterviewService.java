package callofproject.dev.service.interview.service.codinginterview;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.service.interview.dto.coding.CreateCodingInterviewDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author Nuri Can ÖZTÜRK
 * Represents the interface coding interview service.
 * CopyRight(C) 2023 by Call Of Project Teams.
 */
public interface ICodingInterviewService
{
    /**
     * Create a new code interview.
     *
     * @param dto represents the create coding interview dto
     * @return the response message
     */
    ResponseMessage<Object> createCodeInterview(CreateCodingInterviewDTO dto);

    /**
     * Update the code interview.
     *
     * @param codeInterviewId represents the code interview id
     * @param ownerId         represents the owner id
     * @return the response message
     */
    ResponseMessage<Object> deleteCodeInterview(UUID ownerId, UUID codeInterviewId);

    /**
     * Delete the code interview by project id.
     *
     * @param projectId represents the project id
     * @return the response message
     */
    ResponseMessage<Object> deleteCodeInterviewByProjectId(UUID projectId);

    /**
     * Add a participant to the code interview.
     *
     * @param codeInterviewId represents the code interview id
     * @param userId          represents the user id
     * @return the response message
     */
    ResponseMessage<Object> addParticipant(UUID codeInterviewId, UUID userId);

    /**
     * Add a participant to the code interview by project id.
     *
     * @param projectId represents the project id
     * @param userId    represents the user id
     * @return the response message
     */
    ResponseMessage<Object> addParticipantByProjectId(UUID projectId, UUID userId);

    /**
     * Remove a participant from the code interview.
     *
     * @param codeInterviewId represents the code interview id
     * @param userId          represents the user id
     * @return the response message
     */
    ResponseMessage<Object> removeParticipant(UUID codeInterviewId, UUID userId);

    /**
     * Remove a participant from the code interview by project id.
     *
     * @param projectId represents the project id
     * @param userId    represents the user id
     * @return the response message
     */
    ResponseMessage<Object> removeParticipantByProjectId(UUID projectId, UUID userId);

    /**
     * Get the code interview by project id.
     *
     * @param projectId represents the project id
     * @return the response message
     */
    ResponseMessage<Object> getInterviewByProjectId(UUID projectId);

    /**
     * Get the code interview by code interview id.
     *
     * @param codeInterviewId represents the code interview id
     * @return the response message
     */
    ResponseMessage<Object> getInterview(UUID codeInterviewId);

    /**
     * Submit the code interview.
     *
     * @param userId          represents the user id
     * @param codeInterviewId represents the code interview id
     * @param file            represents the file
     * @return the response message
     */
    ResponseMessage<Object> submitInterview(UUID userId, UUID codeInterviewId, MultipartFile file);

    /**
     * Get the code interview by user id.
     *
     * @param userId represents the user id
     * @return the response message
     */
    ResponseMessage<Object> isUserSolvedBefore(UUID userId, UUID interviewId);

    /**
     * Accept the code interview.
     *
     * @param id         represents the code interview id
     * @param isAccepted represents the is accepted
     * @return the response message
     */
    ResponseMessage<Object> acceptInterview(UUID id, boolean isAccepted);

    /**
     * Get the participants by code interview id.
     *
     * @param codeInterviewId represents the code interview id
     * @return the multiple response message
     */
    MultipleResponseMessage<Object> getParticipants(UUID codeInterviewId);

    /**
     * Get the participants by project id.
     *
     * @param projectId represents the project id
     * @return the multiple response message
     */
    MultipleResponseMessage<Object> getParticipantsByProjectId(UUID projectId);

    /**
     * Find the user interview information.
     *
     * @param userId represents the user id
     * @return the multiple response message
     */
    MultipleResponseMessage<Object> findUserInterviewInformation(UUID userId);
}
