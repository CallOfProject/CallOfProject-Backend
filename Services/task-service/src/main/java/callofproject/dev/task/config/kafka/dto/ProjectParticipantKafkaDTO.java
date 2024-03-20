package callofproject.dev.task.config.kafka.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ProjectParticipantKafkaDTO
 */
public record ProjectParticipantKafkaDTO(
        UUID projectParticipantId,
        UUID projectId,
        UUID userId,
        LocalDateTime joinDate,
        boolean isDeleted)
{
    /**
     * toString
     */
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
