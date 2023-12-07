package callofproject.dev.repository.environment.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;


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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseOrganization that = (CourseOrganization) o;
        return Objects.equals(courseOrganizationName, that.courseOrganizationName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(courseOrganizationName);
    }
}
