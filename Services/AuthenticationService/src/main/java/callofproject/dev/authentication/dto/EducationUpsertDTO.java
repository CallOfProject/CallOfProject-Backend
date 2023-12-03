package callofproject.dev.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record EducationUpsertDTO(
        @JsonProperty("university_id")
        long universityId,
        @JsonProperty("school_name")
        String schoolName,
        String department,
        String description,
        @JsonProperty("start_date")
        LocalDate startDate,
        @JsonProperty("finish_date")
        LocalDate finishDate,
        @JsonProperty("is_continue")
        boolean isContinue,
        double gpa

)
{
}
