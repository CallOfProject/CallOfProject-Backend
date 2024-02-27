package callofproject.dev.service.interview.mapper;

import callofproject.dev.service.interview.data.entity.TestInterview;
import callofproject.dev.service.interview.dto.test.CreateTestDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewDTO;
import org.mapstruct.Mapper;

@Mapper(implementationName = "TestInterviewMapperImpl", componentModel = "spring")
public interface ITestInterviewMapper
{
    TestInterview toTestInterview(CreateTestDTO dto);
    TestInterviewDTO toTestInterviewDTO(TestInterview testInterview);
}
