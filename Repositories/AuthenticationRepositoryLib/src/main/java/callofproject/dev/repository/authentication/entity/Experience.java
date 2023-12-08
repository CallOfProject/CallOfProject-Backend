package callofproject.dev.repository.authentication.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "experience")
public class Experience
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "experience_id")
    private UUID experience_id;
    @Column(name = "company_unique_id", nullable = false, length = 50)
    private String companyUniqueId;
    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "website_link", length = 100)
    private String companyWebsiteLink;
    @Column(name = "star_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "finish_date", nullable = false)
    private LocalDate finishDate;
    @Column(name = "is_continue", nullable = false)
    private boolean isContinue;

    @Column(name = "job_definition", length = 200, nullable = false)
    private String jobDefinition;
    @ManyToMany(mappedBy = "experienceList", fetch = FetchType.EAGER)
    private Set<UserProfile> userProfiles;

    public Experience()
    {
    }

    public Experience(String companyUniqueId, String companyName, String description, String companyWebsiteLink,
                      LocalDate startDate, LocalDate finishDate, boolean isContinue, String jobDefinition)
    {
        this.jobDefinition = jobDefinition;
        this.companyUniqueId = companyUniqueId;
        this.companyName = companyName;
        this.description = description;
        this.companyWebsiteLink = companyWebsiteLink;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.isContinue = isContinue;
    }

    public String getJobDefinition()
    {
        return jobDefinition;
    }

    public void setJobDefinition(String jobDefinition)
    {
        this.jobDefinition = jobDefinition;
    }

    public String getCompanyUniqueId()
    {
        return companyUniqueId;
    }

    public void setCompanyUniqueId(String companyUniqueId)
    {
        this.companyUniqueId = companyUniqueId;
    }

    public Set<UserProfile> getUserProfiles()
    {
        return userProfiles;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles)
    {
        this.userProfiles = userProfiles;
    }

    public UUID getExperience_id()
    {
        return experience_id;
    }

    public void setExperience_id(UUID experience_id)
    {
        this.experience_id = experience_id;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getCompanyWebsiteLink()
    {
        return companyWebsiteLink;
    }

    public void setCompanyWebsiteLink(String companyWebsiteLink)
    {
        this.companyWebsiteLink = companyWebsiteLink;
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
}