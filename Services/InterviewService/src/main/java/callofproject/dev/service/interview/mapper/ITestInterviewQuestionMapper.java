package callofproject.dev.service.interview.mapper;


import callofproject.dev.data.interview.entity.TestInterviewQuestion;
import callofproject.dev.service.interview.dto.test.CreateQuestionDTO;
import callofproject.dev.service.interview.dto.test.QuestionDTO;
import org.mapstruct.Mapper;

@Mapper(implementationName = "TestInterviewQuestionMapperImpl", componentModel = "spring")
public interface ITestInterviewQuestionMapper
{
    TestInterviewQuestion toTestInterviewQuestion(CreateQuestionDTO dto);
    QuestionDTO toQuestionDTO(TestInterviewQuestion testInterviewQuestion);
}
