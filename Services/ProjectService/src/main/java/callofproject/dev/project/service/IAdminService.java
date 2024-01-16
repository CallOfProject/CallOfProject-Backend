package callofproject.dev.project.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;

import java.util.UUID;

public interface IAdminService
{
    ResponseMessage<Object> cancelProject(UUID projectId);

    ResponseMessage<Object> cancelProjectCallback(UUID projectId);

    MultipleResponseMessagePageable<Object> findAll(int page);
}
