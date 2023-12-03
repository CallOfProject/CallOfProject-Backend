package callofproject.dev.authentication.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class CourseUpsertDTO
{
    @JsonProperty("course_id")
    private String courseId;
    @JsonProperty("organizator")
    private String organizator;
    @JsonProperty("course_name")
    private String courseName;
    @JsonProperty("start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate startDate;
    @JsonProperty("finish_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate finishDate;
    @JsonProperty("is_continue")
    private boolean isContinue;
    private String description;

    public CourseUpsertDTO(String courseId, String organizator, String courseName, LocalDate startDate, LocalDate finishDate, boolean isContinue, String description)
    {
        this.courseId = courseId;
        this.organizator = organizator;
        this.courseName = courseName;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.isContinue = isContinue;
        this.description = description;
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
