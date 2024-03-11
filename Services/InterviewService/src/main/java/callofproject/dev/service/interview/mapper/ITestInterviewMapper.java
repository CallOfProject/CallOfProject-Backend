package callofproject.dev.service.interview.mapper;

import callofproject.dev.data.interview.entity.TestInterview;
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
    @Mapping(target = "interviewStatus", source = "testInterview.interviewStatus")
    TestInterviewDTO toTestInterviewDTO(TestInterview testInterview, ProjectDTO projectDTO);
}
