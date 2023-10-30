package callofproject.dev.repository.usermanagement.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "course")
public class Course
{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "course_id")
    private UUID course_id;

    @Column(name = "organizator", nullable = false, length = 80)
    private String organizator;
    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;
    @Column(name = "start_date", nullable = false, length = 30)
    private LocalDate startDate;
    @Column(name = "finish_date", nullable = false, length = 30)
    private LocalDate finishDate;
    @Column(name = "is_continue", nullable = false)
    private boolean isContinue;
    @Column(name = "description", length = 500)
    private String description;
    @ManyToMany(mappedBy = "courseList", fetch = FetchType.LAZY)
    private Set<UserProfile> userProfiles;


    public Course()
    {
    }

    public Set<UserProfile> getUserProfiles()
    {
        return userProfiles;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles)
    {
        this.userProfiles = userProfiles;
    }

    public UUID getCourse_id()
    {
        return course_id;
    }

    public void setCourse_id(UUID course_id)
    {
        this.course_id = course_id;
    }

    public String getOrganizator()
    {
        return organizator;
    }

    public void setOrganizator(String organizator)
    {
        this.organizator = organizator;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public LocalDate getStartDate()
    {
        return startDate;
    }

    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }

    public LocalDate getFinishDate()
    {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate)
    {
        this.finishDate = finishDate;
    }

    public boolean isContinue()
    {
        return isContinue;
    }

    public void setContinue(boolean aContinue)
    {
        isContinue = aContinue;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
