package callofproject.dev.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class ExperienceUpsertDTO
{
    @JsonProperty("experience_id")
    private String id;
    @JsonProperty("company_name")
    private String companyName;
    private String description;
    @JsonProperty("company_website")
    private String companyWebsite;
    @JsonProperty("start_date")
    private LocalDate startDate;
    @JsonProperty("finish_date")
    private LocalDate finishDate;
    @JsonProperty("is_continue")
    private boolean isContinue;

    public ExperienceUpsertDTO(String id, String companyName, String description, String companyWebsite, LocalDate startDate, LocalDate finishDate, boolean isContinue)
    {
        this.id = id;
        this.companyName = companyName;
        this.description = description;
        this.companyWebsite = companyWebsite;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.isContinue = isContinue;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
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

    public void setContinue(boolean aContinue)
    {
        isContinue = aContinue;
    }
}
