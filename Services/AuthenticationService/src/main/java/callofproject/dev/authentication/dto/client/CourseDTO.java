package callofproject.dev.authentication.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Data Transfer Object for a course.
 */
public class CourseDTO
{
    @JsonProperty("course_name")
    private String courseName;
    private String id;


    /**
     * Constructor.
     */
    public CourseDTO()
    {
    }


    /**
     * Constructor.
     *
     * @param courseName The course name.
     */
    public CourseDTO(String courseName, String id)
    {
        this.courseName = courseName;
        this.id = id;
    }


    /**
     * Gets the course name.
     *
     * @return The course name.
     */
    public String getCourseName()
    {
        return courseName;
    }


    /**
     * Sets the course name.
     *
     * @param courseName The course name.
     */
    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }


    /**
     * Gets the course id.
     *
     * @return The course id.
     */
    public String getId()
    {
        return id;
    }


    /**
     * Sets the course id.
     *
     * @param id The course id.
     */
    public void setId(String id)
    {
        this.id = id;
    }
}
