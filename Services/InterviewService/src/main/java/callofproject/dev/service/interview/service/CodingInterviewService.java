package callofproject.dev.service.interview.service;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.service.interview.data.dal.InterviewServiceHelper;
import callofproject.dev.service.interview.dto.coding.CreateCodingInterviewDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class CodingInterviewService
{
    private final InterviewServiceHelper m_interviewServiceHelper;

    public CodingInterviewService(InterviewServiceHelper interviewServiceHelper)
    {
        m_interviewServiceHelper = interviewServiceHelper;
    }

    public ResponseMessage<Object> createCodeInterview(CreateCodingInterviewDTO dto)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ResponseMessage<Object> deleteCodeInterview(String codeInterviewId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ResponseMessage<Object> deleteCodeInterviewByProjectId(String projectId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ResponseMessage<Object> addParticipant(String codeInterviewId, String userId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ResponseMessage<Object> addParticipantByProjectId(String projectId, String userId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ResponseMessage<Object> removeParticipant(String codeInterviewId, String userId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public ResponseMessage<Object> removeParticipantByProjectId(String projectId, String userId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public ResponseMessage<Object> getParticipants(String codeInterviewId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public MultipleResponseMessage<Object> getParticipantsByProjectId(String projectId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public ResponseMessage<Object> getInterviewByProjectId(String projectId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public ResponseMessage<Object> getInterview(String codeInterviewId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public MultipleResponseMessage<Object> getAllInterviews()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public MultipleResponseMessage<Object> getAllInterviewsByProjectId(String projectId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
