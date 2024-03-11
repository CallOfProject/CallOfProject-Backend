package callofproject.dev.service.interview.mapper;

import callofproject.dev.data.interview.entity.UserTestInterviews;
import callofproject.dev.service.interview.dto.ProjectDTO;
import callofproject.dev.service.interview.dto.UserDTO;
import callofproject.dev.service.interview.dto.UserTestInterviewDTO;
import callofproject.dev.service.interview.dto.test.QuestionAnswerDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(implementationName = "UserTestInterviewMapperImpl", componentModel = "spring")
public interface IUserTestInterviewMapper
{
    @Mapping(target = "projectDTO", source = "testInterview.projectDTO")
    @Mapping(target = "id", source = "userTestInterview.id")
    @Mapping(target = "interviewStatus", source = "userTestInterview.interviewStatus")
    @Mapping(target = "interviewResult", source = "userTestInterview.interviewResult")
    UserTestInterviewDTO toUserTestInterviewDTO(UserTestInterviews userTestInterview,
                                                TestInterviewDTO testInterview,
                                                List<QuestionAnswerDTO> userAnswers,
                                                ProjectDTO projectDTO,
                                                UserDTO user);
}
