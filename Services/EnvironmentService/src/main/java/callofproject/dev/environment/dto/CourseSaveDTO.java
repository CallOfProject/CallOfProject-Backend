package callofproject.dev.environment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseSaveDTO
{
    @JsonProperty("course_name")
    private String courseName;


    public CourseSaveDTO()
    {
    }

    public CourseSaveDTO(String courseName)
    {
        this.courseName = courseName.toUpperCase();
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }
}
