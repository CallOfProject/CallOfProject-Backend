package callofproject.dev.authentication.dto.environments;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

public class EducationUpsertDTO
{
    @JsonProperty("user_id")
    private UUID userId;
    @JsonProperty("university_id")
    @JsonIgnore
    private String universityId;
    @JsonProperty("school_name")
    private String schoolName;
    @JsonProperty("department")
    private String department;
    @JsonProperty("description")
    private String description;
    @JsonProperty("start_date")
    @Schema(description = "date format: dd/MM/yyyy", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate startDate;
    @JsonProperty("finish_date")
    @Schema(description = "date format: dd/MM/yyyy", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate finishDate;
    @JsonProperty("is_continue")
    @Schema(description = "true/false", type = "boolean")
    private boolean isContinue;
    @JsonProperty("gpa")
    @Schema(description = "gpa like: 3.1, 2.43. 1.53...", type = "double")
    private double gpa;

    public EducationUpsertDTO()
    {
    }

    public UUID getUserId()
    {
        return userId;
    }

    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }

    public String getUniversityId()
    {
        return universityId;
    }

    public void setUniversityId(String universityId)
    {
        this.universityId = universityId;
    }

    public String getSchoolName()
    {
        return schoolName;
    }

    public void setSchoolName(String schoolName)
    {
        this.schoolName = schoolName;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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

    public void setContinue(boolean isContinue)
    {
        this.isContinue = isContinue;
    }

    public double getGpa()
    {
        return gpa;
    }

    public void setGpa(double gpa)
    {
        this.gpa = gpa;
    }
}

