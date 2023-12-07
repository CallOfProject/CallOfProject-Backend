package callofproject.dev.authentication.dto.environments;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record EducationDTO(
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
