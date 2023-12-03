package callofproject.dev.repository.environment.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("course_organization")
public class CourseOrganization
{
    @Id
    private String id;
    @Indexed(unique = true)
    @JsonProperty("course_organization_name")
    private String courseOrganizationName;

    public CourseOrganization(String courseOrganizationName)
    {
        this.courseOrganizationName = courseOrganizationName;
    }

    public CourseOrganization()
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

    public String getCourseOrganizationName()
    {
        return courseOrganizationName;
    }

    public void setCourseOrganizationName(String courseOrganizationName)
    {
        this.courseOrganizationName = courseOrganizationName;
    }
}
