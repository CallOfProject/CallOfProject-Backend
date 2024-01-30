package callofproject.dev.task.config.kafka.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectParticipantKafkaDTO(
        UUID projectParticipantId,
        UUID projectId,
        UUID userId,
        LocalDateTime joinDate)
{
    @Override
    public String toString()
    {
        return "ProjectParticipantDTO{" +
                "projectParticipantId=" + projectParticipantId +
                ", projectId=" + projectId +
                ", userId=" + userId +
                ", joinDate=" + joinDate +
                '}';
    }
}
