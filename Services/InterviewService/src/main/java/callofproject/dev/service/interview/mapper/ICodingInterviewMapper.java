package callofproject.dev.service.interview.mapper;

import callofproject.dev.service.interview.data.entity.CodingInterview;
import callofproject.dev.service.interview.dto.coding.CreateCodingInterviewDTO;
import org.mapstruct.Mapper;

@Mapper(implementationName = "CodingInterviewMapperImpl", componentModel = "spring")
public interface ICodingInterviewMapper
{
    CodingInterview toCodingInterview(CreateCodingInterviewDTO dto);
}
