package callofproject.dev.environment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CourseDTO
 */
public class CourseDTO
{
    @JsonProperty("course_name")
    private String courseName;

    @JsonProperty("course_id")
    private String id;


    /**
     * Constructor
     */
    public CourseDTO()
    {
    }


    /**
     * Constructor
     *
     * @param courseName course name
     */
    public CourseDTO(String courseName)
    {
        this.courseName = courseName.toUpperCase();
    }


    /**
     * Getter
     *
     * @return course name
     */
    public String getCourseName()
    {
        return courseName;
    }


    /**
     * Setter
     *
     * @param courseName course name
     */
    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }


    /**
     * Getter
     *
     * @return course id
     */
    public String getId()
    {
        return id;
    }


    /**
     * Setter
     *
     * @param id course id
     */
    public void setId(String id)
    {
        this.id = id;
    }
}
