package callofproject.dev.authentication.dto.user_profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object for an experience.
 */
public record ExperienceDTO(
        @JsonProperty("experience_id")
        UUID experienceId,

        @JsonProperty("company_name")
        String companyName,

        String description,

        @JsonProperty("company_website_link")
        String companyWebsiteLink,

        @JsonProperty("start_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate startDate,

        @JsonProperty("finish_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate finishDate,

        @JsonProperty("is_continue")
        boolean isContinue,

        String position
)
{
}
