package callofproject.dev.service.interview.service.management;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.interview.entity.TestInterview;
import callofproject.dev.data.interview.entity.UserTestInterviews;
import callofproject.dev.service.interview.dto.ProjectDTO;
import callofproject.dev.service.interview.dto.UserTestInterviewDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;

@Service
@Lazy
public class InterviewManagementService implements IInterviewManagementService
{
    private final InterviewManagementCallbackService m_managementCallbackService;

    public InterviewManagementService(InterviewManagementCallbackService managementCallbackService)
    {
        m_managementCallbackService = managementCallbackService;
    }

    @Override
    public MultipleResponseMessage<Object> findAllInterviewsByUserId(UUID userId)
    {
        return doForDataService(() -> m_managementCallbackService.findAllInterviewsByUserId(userId), "InterviewManagementService.findAllInterviewsByUserId");
    }

    @Override
    public ResponseMessage<Object> findCodingInterviewOwner(UUID interviewId)
    {
        return doForDataService(() -> m_managementCallbackService.findCodingInterviewOwner(interviewId), "InterviewManagementService.findCodingInterviewOwner");
    }

    @Override
    public ResponseMessage<Object> findTestInterviewOwner(UUID interviewId)
    {
        return doForDataService(() -> m_managementCallbackService.findTestInterviewOwner(interviewId), "InterviewManagementService.findTestInterviewOwner");
    }

    @Override
    public UserTestInterviewDTO toUserTestInterviewDTO(UserTestInterviews uti, TestInterview testInterview, ProjectDTO projectDTO, TestInterviewDTO testInterviewDTO)
    {
        return doForDataService(() -> m_managementCallbackService.toUserTestInterviewDTO(uti, testInterview, projectDTO, testInterviewDTO), "InterviewManagementService.toUserTestInterviewDTO");
    }
}
