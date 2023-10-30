package callofproject.dev.repository.usermanagement.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "education")
public class Education
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "education_id")
    private UUID education_id;
    @Column(name = "school_name", nullable = false, length = 80)
    private String schoolName;
    @Column(name = "department", nullable = false, length = 80)
    private String department;
    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "finish_date", nullable = false)
    private LocalDate finishDate;

    @Column(name = "is_continue", nullable = false)
    private boolean isContinue;

    @ManyToMany(mappedBy = "educationList", fetch = FetchType.LAZY)
    private Set<UserProfile> userProfiles;

    public Education()
    {
    }

    public UUID getEducation_id()
    {
        return education_id;
    }

    public void setEducation_id(UUID education_id)
    {
        this.education_id = education_id;
    }

    public String getSchoolName()
    {
        return schoolName;
    }

    public void setSchoolName(String schoolName)
    {
        this.schoolName = schoolName;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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

    public Set<UserProfile> getUserProfiles()
    {
        return userProfiles;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles)
    {
        this.userProfiles = userProfiles;
    }
}
