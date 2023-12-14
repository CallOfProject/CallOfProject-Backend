package callofproject.dev.authentication.dto.environments;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public class CourseUpsertDTO
{
    @JsonProperty("user_id")
    @NotNull
    private UUID userId;

    @JsonProperty("course_id")
    @JsonIgnore
    private String courseId;

    @JsonProperty("organizator")
    private String organizator;

    @JsonProperty("course_name")
    @NotBlank
    @Size(max = 255)
    private String courseName;

    @JsonProperty("start_date")
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Schema(description = "date format: dd/MM/yyyy", type = "string")
    private LocalDate startDate;

    @JsonProperty("finish_date")
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Schema(description = "date format: dd/MM/yyyy", type = "string")
    private LocalDate finishDate;

    @JsonProperty("is_continue")
    private boolean isContinue;

    @Size(max = 255)
    private String description;

    public CourseUpsertDTO(UUID userId, String organizator, String courseName, LocalDate startDate, LocalDate finishDate, boolean isContinue, String description)
    {
        this.userId = userId;
        this.organizator = organizator;
        this.courseName = courseName;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.isContinue = isContinue;
        this.description = description;
    }

    public UUID getUserId()
    {
        return userId;
    }

    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }

    public String getCourseId()
    {
        return courseId;
    }

    public void setCourseId(String courseId)
    {
        this.courseId = courseId;
    }

    public String getOrganizator()
    {
        return organizator;
    }

    public void setOrganizator(String organizator)
    {
        this.organizator = organizator;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public LocalDate getStartDate()
    {
        return startDate;
    }

    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }

    public LocalDate getFinishDate()
    {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate)
    {
        this.finishDate = finishDate;
    }

    public boolean isContinue()
    {
        return isContinue;
    }

    public void setContinue(boolean aContinue)
    {
        isContinue = aContinue;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
