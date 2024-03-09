package callofproject.dev.service.interview.dto;

import callofproject.dev.service.interview.dto.coding.CodingInterviewDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewDTO;

import java.util.List;

public record CodingAndTestInterviewsDTO(List<CodingInterviewDTO> codingInterviews,
                                         List<TestInterviewDTO> testInterviews)
{
}
