package callofproject.dev.project.dto;

import java.util.UUID;

public record NotificationObject(
        UUID projectId,
        UUID userId
)
{
}
