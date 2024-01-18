package callofproject.dev.authentication.dto.environments;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object for an education.
 */
public class EducationUpsertDTO
{
    @NotNull
    @Parameter(description = "User ID")
    @JsonProperty("user_id")
    private UUID userId;
    @JsonProperty("university_id")
    @JsonIgnore
    private String universityId;

    @Parameter(description = "School Name (between 2 and 100 characters)")
    @JsonProperty("school_name")
    @NotBlank
    @Size(min = 2, max = 100)
    private String schoolName;
    @JsonProperty("department")
    @NotBlank
    @Size(min = 2, max = 100)
    @Parameter(description = "Department (between 2 and 100 characters)")
    private String department;
    @JsonProperty("description")
    @Size(max = 255)
    @Parameter(description = "Description (up to 255 characters)")
    private String description;

    @Parameter(description = "Start Date (dd/MM/yyyy)", example = "01/01/2023")
    @JsonProperty("start_date")
    @Schema(description = "date format: dd/MM/yyyy", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @NotNull
    @Past
    private LocalDate startDate;

    @Parameter(description = "Finish Date (dd/MM/yyyy)", example = "31/12/2023")
    @JsonProperty("finish_date")
    @Schema(description = "date format: dd/MM/yyyy", type = "string")
    @NotNull
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate finishDate;
    @Parameter(description = "Is Continue (true or false)", example = "true")
    @JsonProperty("is_continue")
    @Schema(description = "true/false", type = "boolean")
    @NotNull
    private boolean isContinue;

    @Parameter(description = "GPA (e.g., 3.1)", example = "3.1")
    @JsonProperty("gpa")
    @DecimalMin("0.0")
    @DecimalMax("4.0")
    @Digits(integer = 1, fraction = 2)
    private double gpa;

    /**
     * Constructor.
     *
     * @param userId      user ID
     * @param schoolName  school name
     * @param department  department
     * @param description description
     * @param startDate   start date
     * @param finishDate  finish date
     * @param isContinue  is continue
     * @param gpa         gpa
     */
    public EducationUpsertDTO(UUID userId, String schoolName, String department, String description, LocalDate startDate, LocalDate finishDate, boolean isContinue, double gpa)
    {
        this.userId = userId;
        this.schoolName = schoolName;
        this.department = department;
        this.description = description;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.isContinue = isContinue;
        this.gpa = gpa;
    }

    /**
     * Constructor.
     */
    public EducationUpsertDTO()
    {
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
     * Getter for university ID.
     *
     * @return university ID
     */
    public String getUniversityId()
    {
        return universityId;
    }


    /**
     * Setter for university ID.
     *
     * @param universityId university ID
     */
    public void setUniversityId(String universityId)
    {
        this.universityId = universityId;
    }


    /**
     * Getter for school name.
     *
     * @return school name
     */
    public String getSchoolName()
    {
        return schoolName;
    }


    /**
     * Setter for school name.
     *
     * @param schoolName school name
     */
    public void setSchoolName(String schoolName)
    {
        this.schoolName = schoolName;
    }


    /**
     * Getter for department.
     *
     * @return department
     */
    public String getDepartment()
    {
        return department;
    }


    /**
     * Setter for department.
     *
     * @param department department
     */
    public void setDepartment(String department)
    {
        this.department = department;
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
     * @param isContinue is continue
     */
    public void setContinue(boolean isContinue)
    {
        this.isContinue = isContinue;
    }


    /**
     * Getter for gpa.
     *
     * @return gpa
     */
    public double getGpa()
    {
        return gpa;
    }


    /**
     * Setter for gpa.
     *
     * @param gpa gpa
     */
    public void setGpa(double gpa)
    {
        this.gpa = gpa;
    }
}

