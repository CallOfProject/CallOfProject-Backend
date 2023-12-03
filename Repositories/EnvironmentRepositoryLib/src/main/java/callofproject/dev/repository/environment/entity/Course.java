package callofproject.dev.repository.environment.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("course")
public class Course
{
    @Id
    private String id;

    @Indexed(unique = true)
    @JsonProperty("course_name")
    private String courseName;

    public Course(String courseName)
    {
        this.courseName = courseName;
    }

    public Course()
    {
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
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
}
