package callofproject.dev.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record ProjectParticipantDTO(
        String username,
        @JsonProperty("full_name")
        String fullName,
        @JsonProperty("join_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate joinDate
)
{
}
