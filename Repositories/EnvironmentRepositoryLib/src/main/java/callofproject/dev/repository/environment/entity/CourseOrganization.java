package callofproject.dev.repository.environment.entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("course_organization")
public class CourseOrganization
{
    @Id
    private String courseOrganizationId;

    @Indexed(unique = true)
    private String courseOrganizationName;

    public CourseOrganization(String courseOrganizationName)
    {
        this.courseOrganizationName = courseOrganizationName;
    }

    public CourseOrganization()
    {
    }

    public String getCourseOrganizationId()
    {
        return courseOrganizationId;
    }

    public void setCourseOrganizationId(String courseOrganizationId)
    {
        this.courseOrganizationId = courseOrganizationId;
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
