package callofproject.dev.environment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseDTO
{
    @JsonProperty("course_name")
    private String courseName;

    @JsonProperty("course_id")
    private String id;

    public CourseDTO()
    {
    }

    public CourseDTO(String courseName)
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

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
