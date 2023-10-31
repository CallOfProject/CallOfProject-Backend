package callofproject.dev.repository.environment.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "course_organizator")
public class CourseOrganizator
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_organizator_id")
    private long courseOrganizatorId;

    @Column(name = "organizator_name", unique = true, nullable = false)
    private String organizatorName;

    public CourseOrganizator(String organizatorName)
    {
        this.organizatorName = organizatorName;
    }

    public CourseOrganizator()
    {
    }

    public long getCourseOrganizatorId()
    {
        return courseOrganizatorId;
    }

    public void setCourseOrganizatorId(long courseOrganizatorId)
    {
        this.courseOrganizatorId = courseOrganizatorId;
    }

    public String getOrganizatorName()
    {
        return organizatorName;
    }

    public void setOrganizatorName(String organizatorName)
    {
        this.organizatorName = organizatorName;
    }
}
