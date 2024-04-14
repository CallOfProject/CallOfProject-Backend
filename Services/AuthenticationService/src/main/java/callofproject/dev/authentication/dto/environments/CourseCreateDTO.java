package callofproject.dev.authentication.dto.environments;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object for a course.
 */
public class CourseCreateDTO
{
    @JsonProperty("user_id")
    @NotNull
    private UUID userId;

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

    /**
     * Constructor.
     *
     * @param userId      user ID
     * @param organizator organizator
     * @param courseName  course name
     * @param startDate   start date
     * @param finishDate  finish date
     * @param isContinue  is continue
     * @param description description
     */
    public CourseCreateDTO(UUID userId, String organizator, String courseName, LocalDate startDate, LocalDate finishDate, boolean isContinue, String description)
    {
        this.userId = userId;
        this.organizator = organizator;
        this.courseName = courseName;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.isContinue = isContinue;
        this.description = description;
    }


    /**
     * Getter for user ID.
     *
     * @return user ID
     */
    public UUID getUserId()
    {
        return userId;
    }


    /**
     * Setter for user ID.
     *
     * @param userId user ID
     */
    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }


    /**
     * Getter for organizator.
     *
     * @return organizator
     */
    public String getOrganizator()
    {
        return organizator;
    }


    /**
     * Setter for organizator.
     *
     * @param organizator organizator
     */
    public void setOrganizator(String organizator)
    {
        this.organizator = organizator;
    }


    /**
     * Getter for course name.
     *
     * @return course name
     */
    public String getCourseName()
    {
        return courseName;
    }


    /**
     * Setter for course name.
     *
     * @param courseName course name
     */
    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }


    /**
     * Getter for start date.
     *
     * @return start date
     */
    public LocalDate getStartDate()
    {
        return startDate;
    }


    /**
     * Setter for start date.
     *
     * @param startDate start date
     */
    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }


    /**
     * Getter for finish date.
     *
     * @return finish date
     */
    public LocalDate getFinishDate()
    {
        return finishDate;
    }


    /**
     * Setter for finish date.
     *
     * @param finishDate finish date
     */
    public void setFinishDate(LocalDate finishDate)
    {
        this.finishDate = finishDate;
    }


    /**
     * Getter for is continue.
     *
     * @return is continue
     */
    public boolean isContinue()
    {
        return isContinue;
    }


    /**
     * Setter for is continue.
     *
     * @param aContinue is continue
     */
    public void setContinue(boolean aContinue)
    {
        isContinue = aContinue;
    }


    /**
     * Getter for description.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Setter for description.
     *
     * @param description description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
}
