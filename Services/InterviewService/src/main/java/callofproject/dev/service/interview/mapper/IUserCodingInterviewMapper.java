package callofproject.dev.service.interview.mapper;

import callofproject.dev.data.interview.entity.UserCodingInterviews;
import callofproject.dev.service.interview.dto.ProjectDTO;
import callofproject.dev.service.interview.dto.UserDTO;
import callofproject.dev.service.interview.dto.coding.CodingInterviewDTO;
import callofproject.dev.service.interview.dto.test.UserCodingInterviewDTOV2;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(implementationName = "UserCodingInterviewMapperImpl", componentModel = "spring")
public interface IUserCodingInterviewMapper
{
    @Mapping(target = "projectDTO", source = "codingInterviewDTO.projectDTO")
    @Mapping(target = "interviewStatus", source = "userCodingInterviews.interviewStatus")
    UserCodingInterviewDTOV2 toUserCodingInterviewDTOV2(UserCodingInterviews userCodingInterviews, CodingInterviewDTO codingInterviewDTO,
                                                        ProjectDTO projectDTO, UserDTO user);
}
