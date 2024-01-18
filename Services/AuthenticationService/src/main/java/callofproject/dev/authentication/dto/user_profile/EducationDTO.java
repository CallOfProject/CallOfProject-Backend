package callofproject.dev.authentication.dto.user_profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object for an education.
 */
public record EducationDTO(
        @JsonProperty("education_id")
        UUID educationId,
        @JsonProperty("school_name")
        String schoolName,
        @JsonProperty("department")
        String department,
        @JsonProperty("description")
        String description,
        @JsonProperty("start_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate startDate,
        @JsonProperty("finish_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate finishDate,
        @JsonProperty("is_continue")
        boolean isContinue,
        @JsonProperty("gpa")
        double gpa
)
{
}
