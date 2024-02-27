package callofproject.dev.service.interview.dto.coding;

import callofproject.dev.service.interview.dto.ProjectDTO;

public record CodingInterviewDTO(
        String title,
        String description,
        long durationMinutes,
        String question,
        String answerFileName,
        int point,
        ProjectDTO projectDTO
)
{
}
