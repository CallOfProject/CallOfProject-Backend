package callofproject.dev.service.interview.service.management;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;

import java.util.UUID;

public interface IInterviewManagementService
{
    MultipleResponseMessage<Object> findAllInterviewsByUserId(UUID userId);

    ResponseMessage<Object> findCodingInterviewOwner(UUID interviewId);

    ResponseMessage<Object> findTestInterviewOwner(UUID interviewId);
}
