package callofproject.dev.project.dto;

import callofproject.dev.data.project.entity.*;

import java.time.LocalDate;

public record ProjectSave(
        String projectImage,
        String projectName,
        String projectSummary,
        String projectDescription,
        String projectAim,
        LocalDate applicationDeadline,
        LocalDate expectedCompletionDate,
        LocalDate expectedProjectDeadline,
        int maxParticipantCount,
        String technicalRequirements,
        String specialRequirements,
        ProjectAccessType projectAccessType,
        ProjectProfessionLevel professionLevel,
        Sector sector,
        Degree degree,
        ProjectLevel projectLevel,
        InterviewType interviewType
)
{

}
