package callofproject.dev.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

public record ProjectParticipantDTO(
        @JsonProperty("user_id")
        UUID userId,
        @JsonProperty("project_id")
        UUID projectId,
        String username,
        @JsonProperty("full_name")
        String fullName,
        @JsonProperty("join_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate joinDate
)
{
}
