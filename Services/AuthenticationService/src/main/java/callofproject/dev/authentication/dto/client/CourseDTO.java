package callofproject.dev.authentication.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseDTO
{
    @JsonProperty("course_name")
    private String courseName;
    private String id;

    public CourseDTO()
    {
    }

    public CourseDTO(String courseName, String id)
    {
        this.courseName = courseName;
        this.id = id;
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
