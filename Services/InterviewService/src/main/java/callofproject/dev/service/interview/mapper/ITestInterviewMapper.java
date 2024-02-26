package callofproject.dev.service.interview.mapper;

import callofproject.dev.service.interview.data.entity.TestInterview;
import callofproject.dev.service.interview.dto.coding.CreateTestInterviewDTO;
import callofproject.dev.service.interview.dto.test.CreateTestDTO;
import org.mapstruct.Mapper;

@Mapper(implementationName = "TestInterviewMapperImpl", componentModel = "spring")
public interface ITestInterviewMapper
{
    TestInterview toTestInterview(CreateTestDTO dto);
}
