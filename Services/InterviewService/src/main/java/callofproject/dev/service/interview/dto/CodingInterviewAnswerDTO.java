package callofproject.dev.service.interview.dto;

import callofproject.dev.data.interview.entity.UserCodingInterviews;
import callofproject.dev.service.interview.dto.coding.CodingInterviewDTO;

public record CodingInterviewAnswerDTO(UserCodingInterviews userCodingInterviews, CodingInterviewDTO codingInterviewDTO)
{
}
