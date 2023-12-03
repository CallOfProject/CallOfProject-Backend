package callofproject.dev.repository.authentication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "course_organization")
public class CourseOrganization
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "course_organization_id")
    private UUID courseOrganizationId;

    @Column(name = "course_organization_name", nullable = false, length = 100)
    private String courseOrganizationName;

    @OneToMany(mappedBy = "courseOrganization", fetch = FetchType.EAGER, cascade = {DETACH, MERGE, PERSIST, REFRESH})
    @JsonIgnore
    private Set<Course> courses;

    public CourseOrganization()
    {
    }


    public CourseOrganization(String courseOrganizationName)
    {
        this.courseOrganizationName = courseOrganizationName;
    }

    public void addCourseOrganization(Course course)
    {
        if (courses == null)
            courses = new HashSet<>();

        courses.add(course);
    }

    public Set<Course> getCourses()
    {
        return courses;
    }

    public void setCourses(Set<Course> courses)
    {
        this.courses = courses;
    }

    public String getCourseOrganizationName()
    {
        return courseOrganizationName;
    }

    public void setCourseOrganizationName(String courseOrganizationName)
    {
        this.courseOrganizationName = courseOrganizationName;
    }

    public UUID getCourseOrganizationId()
    {
        return courseOrganizationId;
    }

    public void setCourseOrganizationId(UUID courseOrganizationId)
    {
        this.courseOrganizationId = courseOrganizationId;
    }
}
