package callofproject.dev.project;

import callofproject.dev.data.project.entity.enums.*;
import callofproject.dev.nosql.entity.ProjectTag;
import callofproject.dev.project.dto.ParticipantRequestDTO;
import callofproject.dev.project.dto.ProjectSaveDTO;
import callofproject.dev.project.dto.ProjectUpdateDTO;
import callofproject.dev.project.dto.SaveProjectParticipantDTO;
import callofproject.dev.project.dto.overview.ProjectOverviewDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class DataProvider
{
    private DataProvider()
    {
    }

    public static ProjectOverviewDTO provideProjectOverviewDTO()
    {
        return new ProjectOverviewDTO(
                "123456",
                "path/to/image.jpg",
                "Sample Project",
                "This is a sample project summary.",
                "The aim of this project is to...",
                LocalDate.of(2023, 12, 31),
                LocalDate.of(2024, 12, 31),
                LocalDate.of(2023, 1, 1),
                10,
                "Technical requirements go here.",
                "Special requirements go here.",
                EProjectProfessionLevel.Expert,
                EDegree.BACHELOR,
                EProjectLevel.INTERMEDIATE,
                EInterviewType.TEST,
                "John Doe",
                EFeedbackTimeRange.ONE_MONTH,
                EProjectStatus.IN_PROGRESS,
                List.of(
                        new ProjectTag("Tag1", UUID.randomUUID()),
                        new ProjectTag("Tag2", UUID.randomUUID())
                )
        );
    }

    public static ProjectUpdateDTO projectUpdateDTO()
    {
        return new ProjectUpdateDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "path/to/image.jpg",
                "Sample Project",
                "This is a sample project summary.",
                "This is a sample project description.",
                "The aim of this project is to...",
                LocalDate.of(2023, 12, 31),
                LocalDate.of(2024, 12, 31),
                10,
                "Technical requirements go here.",
                "Special requirements go here.",
                EProjectAccessType.PUBLIC,
                EProjectProfessionLevel.Expert,
                ESector.Finance,
                EDegree.MASTER,
                EProjectLevel.INTERMEDIATE,
                EInterviewType.TEST,
                EFeedbackTimeRange.ONE_WEEK,
                LocalDate.of(2023, 1, 1),
                List.of("tag1", "tag2")
        );
    }

    public static ProjectSaveDTO provideProjectSaveDTO()
    {
        return new ProjectSaveDTO(
                UUID.randomUUID(),
                "path/to/image.jpg",
                "Sample Project",
                "This is a sample project summary.",
                "This is a sample project description.",
                "The aim of this project is to...",
                LocalDate.of(2023, 12, 31),
                LocalDate.of(2024, 12, 31),
                LocalDate.of(2023, 1, 1),
                10,
                "Technical requirements go here.",
                "Special requirements go here.",
                EProjectAccessType.PUBLIC,
                EProjectProfessionLevel.Expert,
                ESector.IT,
                EDegree.BACHELOR,
                EProjectLevel.INTERMEDIATE,
                EInterviewType.TEST,
                EFeedbackTimeRange.ONE_MONTH,
                List.of("tag1", "tag2"));
    }

    public static SaveProjectParticipantDTO provideSaveProjectParticipantDTO()
    {
        return new SaveProjectParticipantDTO(UUID.randomUUID(), UUID.randomUUID());
    }

    public static ParticipantRequestDTO provideApproveParticipantRequestDTO()
    {
        return new ParticipantRequestDTO(UUID.randomUUID(), true);
    }

    public static ParticipantRequestDTO provideRejectParticipantRequestDTO()
    {
        return new ParticipantRequestDTO(UUID.randomUUID(), false);
    }
}
