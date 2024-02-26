package callofproject.dev.service.interview.mapper;


import callofproject.dev.service.interview.data.entity.TestInterviewQuestion;
import callofproject.dev.service.interview.dto.test.CreateQuestionDTO;
import org.mapstruct.Mapper;

@Mapper(implementationName = "TestInterviewQuestionMapperImpl", componentModel = "spring")
public interface ITestInterviewQuestionMapper
{
    TestInterviewQuestion toTestInterviewQuestion(CreateQuestionDTO dto);
}
