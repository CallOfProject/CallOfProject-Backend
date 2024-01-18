package callofproject.dev.authentication.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Data Transfer Object for a course.
 */
public class CourseSaveDTO
{
    @JsonProperty("course_name")
    private String courseName;


    /**
     * Constructor.
     */
    public CourseSaveDTO()
    {
    }


    /**
     * Constructor.
     *
     * @param courseName The course name.
     */
    public CourseSaveDTO(String courseName)
    {
        this.courseName = courseName.toUpperCase();
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
}
