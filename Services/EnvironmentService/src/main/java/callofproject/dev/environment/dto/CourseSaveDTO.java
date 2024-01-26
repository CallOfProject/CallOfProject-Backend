package callofproject.dev.environment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CourseSaveDTO
 */
public class CourseSaveDTO
{
    @JsonProperty("course_name")
    private String courseName;

    /**
     * Constructor
     */
    public CourseSaveDTO()
    {
    }


    /**
     * Constructor
     *
     * @param courseName course name
     */
    public CourseSaveDTO(String courseName)
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
}
