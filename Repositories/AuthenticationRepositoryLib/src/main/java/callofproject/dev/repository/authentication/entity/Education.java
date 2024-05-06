package callofproject.dev.repository.authentication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "university_id", nullable = false)
    private String universityId;

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

    @Column(name = "gpa", nullable = false)
    private double gpa;

    @ManyToMany(mappedBy = "educationList", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<UserProfile> userProfiles;

    public Education()
    {
    }


    public Education(String universityId, String schoolName, String department, String description, LocalDate startDate, LocalDate finishDate, boolean isContinue, double gpa)
    {
        this.gpa = gpa;
        this.universityId = universityId;
        this.schoolName = schoolName;
        this.department = department;
        this.description = description;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.isContinue = isContinue;
    }

    public double getGpa()
    {
        return gpa;
    }

    public void setGpa(double gpa)
    {
        this.gpa = gpa;
    }

    public String getUniversityId()
    {
        return universityId;
    }

    public void setUniversityId(String universityId)
    {
        this.universityId = universityId;
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

    public boolean getIsContinue()
    {
        return isContinue;
    }

    public void setIsContinue(boolean aContinue)
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