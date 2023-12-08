package callofproject.dev.authentication.dto.environments;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

public class ExperienceUpsertDTO
{
    @JsonProperty("user_id")
    private UUID userId;
    @JsonProperty("company_name")
    private String companyName;
    private String description;
    @JsonProperty("company_website")
    private String companyWebsite;

    @JsonProperty("job_definition")
    private String jobDefinition;
    @JsonProperty("start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate startDate;
    @JsonProperty("finish_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate finishDate;
    @JsonProperty("is_continue")
    private boolean isContinue;

    public ExperienceUpsertDTO(UUID userId, String companyName, String description, String companyWebsite,
                               LocalDate startDate, LocalDate finishDate, boolean isContinue, String jobDefinition)
    {
        this.jobDefinition = jobDefinition;
        this.userId = userId;
        this.companyName = companyName;
        this.description = description;
        this.companyWebsite = companyWebsite;
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

    public UUID getUserId()
    {
        return userId;
    }

    public void setUserId(UUID userId)
    {
        this.userId = userId;
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

    public String getCompanyWebsite()
    {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite)
    {
        this.companyWebsite = companyWebsite;
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

    public void setIsContinue(boolean aContinue)
    {
        isContinue = aContinue;
    }
}
