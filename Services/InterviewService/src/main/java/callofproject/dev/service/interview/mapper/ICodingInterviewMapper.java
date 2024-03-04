package callofproject.dev.service.interview.mapper;

import callofproject.dev.data.interview.entity.CodingInterview;
import callofproject.dev.service.interview.dto.ProjectDTO;
import callofproject.dev.service.interview.dto.coding.CodingInterviewDTO;
import callofproject.dev.service.interview.dto.coding.CreateCodingInterviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(implementationName = "CodingInterviewMapperImpl", componentModel = "spring")
public interface ICodingInterviewMapper
{
    CodingInterview toCodingInterview(CreateCodingInterviewDTO dto);

    @Mapping(target = "projectDTO", source = "projectDTO")
    CodingInterviewDTO toCodingInterviewDTO(CodingInterview codingInterview, ProjectDTO projectDTO);
}
