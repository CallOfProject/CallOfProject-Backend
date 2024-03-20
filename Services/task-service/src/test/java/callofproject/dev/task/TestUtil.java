package callofproject.dev.task;

import callofproject.dev.data.common.enums.AdminOperationStatus;
import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.data.task.entity.Project;
import callofproject.dev.data.task.entity.ProjectParticipant;
import callofproject.dev.data.task.entity.Role;
import callofproject.dev.data.task.entity.User;
import callofproject.dev.data.task.entity.enums.EProjectStatus;
import callofproject.dev.data.task.entity.enums.Priority;
import callofproject.dev.data.task.entity.enums.TaskStatus;
import callofproject.dev.task.config.kafka.dto.ProjectInfoKafkaDTO;
import callofproject.dev.task.config.kafka.dto.ProjectParticipantKafkaDTO;
import callofproject.dev.task.config.kafka.dto.UserKafkaDTO;
import callofproject.dev.task.dto.request.CreateTaskDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public final class TestUtil
{
    private static final Random RANDOM = new Random();

    private TestUtil()
    {
    }

    public static UserKafkaDTO provideUserKafkaDTO(String username, String email)
    {
        // Örnek roller oluştur
        Role role1 = new Role("ROLE_USER");

        // Roller setini oluştur
        Set<Role> roles = new HashSet<>();
        roles.add(role1);

        // UserKafkaDTO örneği oluştur

        // Örneği yazdır
        return new UserKafkaDTO(
                UUID.randomUUID(), // user_id
                username,
                email,
                "John",
                "Doe",
                "Smith",
                EOperation.CREATE, // operation
                "password123",
                null,
                LocalDateTime.now(), // deletedAt
                2, // ownerProjectCount
                3, // participantProjectCount
                5 // totalProjectCount
        );
    }


    public static User provideUser(UUID id, String username, String email, Set<Role> roles)
    {

        return new User(id, username, email, "middle",
                "firstName", "lastName", roles);
    }

    public static ProjectInfoKafkaDTO provideProjectInfoKafkaDTO(UUID projectId, UserKafkaDTO owner, List<ProjectParticipantKafkaDTO> participants)
    {
        var number = RANDOM.nextInt(100);
        return new ProjectInfoKafkaDTO(projectId, "project-" + number, owner, participants,
                EProjectStatus.IN_PROGRESS, AdminOperationStatus.ACTIVE, EOperation.CREATE);
    }

    public static ProjectParticipantKafkaDTO provideProjectParticipantKafkaDTO(UUID projectId, UUID userId)
    {
        return new ProjectParticipantKafkaDTO(UUID.randomUUID(), projectId, userId, LocalDateTime.now(), false);
    }

    public static ProjectParticipant provideProjectParticipant(UUID projectParticipantId, Project project, User user)
    {
        return new ProjectParticipant(projectParticipantId, project, user, LocalDateTime.now());
    }

    public static Project provideProject(UUID projectId, String projectName, User owner)
    {
        return new Project(projectId, projectName, owner);
    }

    public static CreateTaskDTO provideCreateTaskDTO(UUID projectId, String title, String description, List<UUID> userIds, Priority priority, TaskStatus taskStatus, LocalDate startDate, LocalDate endDate)
    {
        return new CreateTaskDTO(projectId, title, description, userIds, priority, taskStatus, startDate, endDate);
    }

}
