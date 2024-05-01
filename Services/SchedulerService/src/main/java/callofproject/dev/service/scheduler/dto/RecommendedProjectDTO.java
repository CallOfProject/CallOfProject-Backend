package callofproject.dev.service.scheduler.dto;

import java.util.UUID;

public record RecommendedProjectDTO(
        UUID projectId,
        String projectName,
        String projectImage,
        String link,
        String summary
)
{
}
