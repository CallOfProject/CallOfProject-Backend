package callofproject.dev.service.interview.mapper;

import callofproject.dev.service.interview.data.entity.TestInterview;
import callofproject.dev.service.interview.dto.ProjectDTO;
import callofproject.dev.service.interview.dto.test.CreateTestDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(implementationName = "TestInterviewMapperImpl", componentModel = "spring")
public interface ITestInterviewMapper
{
    TestInterview toTestInterview(CreateTestDTO dto);

    @Mapping(target = "projectDTO", source = "projectDTO")
    TestInterviewDTO toTestInterviewDTO(TestInterview testInterview, ProjectDTO projectDTO);
}
