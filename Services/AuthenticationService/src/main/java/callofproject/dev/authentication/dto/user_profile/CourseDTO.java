package callofproject.dev.authentication.dto.user_profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object for a course.
 */
public record CourseDTO(
        @JsonProperty("course_id")
        UUID courseId,
        @JsonProperty("organization")
        String organization,
        @JsonProperty("course_name")
        String courseName,
        @JsonProperty("start_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate startDate,
        @JsonProperty("finish_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate finishDate,
        @JsonProperty("is_continue")
        boolean isContinue,
        String description)
{
}
