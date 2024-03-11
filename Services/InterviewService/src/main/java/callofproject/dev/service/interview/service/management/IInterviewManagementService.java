package callofproject.dev.service.interview.service.management;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.interview.entity.TestInterview;
import callofproject.dev.data.interview.entity.UserTestInterviews;
import callofproject.dev.service.interview.dto.ProjectDTO;
import callofproject.dev.service.interview.dto.UserTestInterviewDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewDTO;

import java.util.UUID;

public interface IInterviewManagementService
{
    MultipleResponseMessage<Object> findAllInterviewsByUserId(UUID userId);

    ResponseMessage<Object> findCodingInterviewOwner(UUID interviewId);

    ResponseMessage<Object> findTestInterviewOwner(UUID interviewId);

    UserTestInterviewDTO toUserTestInterviewDTO(UserTestInterviews uti, TestInterview testInterview, ProjectDTO projectDTO, TestInterviewDTO testInterviewDTO);
}
