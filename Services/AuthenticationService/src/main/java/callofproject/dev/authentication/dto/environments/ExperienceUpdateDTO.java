package callofproject.dev.authentication.dto.environments;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object for an experience.
 */
public class ExperienceUpdateDTO
{
    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("experience_id")
    private UUID experienceId;


    @JsonProperty("company_name")
    @NotBlank(message = "Company name cannot be blank")
    private String companyName;

    private String description;

    @JsonProperty("company_website")
    @Pattern(regexp = "^(http|https)://.*$", message = "Invalid URL format. It should start with http:// or https://")
    private String companyWebsite;

    @JsonProperty("job_definition")
    private String jobDefinition;

    @JsonProperty("start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Schema(description = "date format: dd/MM/yyyy", type = "string")
    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    @JsonProperty("finish_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Schema(description = "date format: dd/MM/yyyy", type = "string")
    @FutureOrPresent(message = "Finish date must be in the future or present")
    private LocalDate finishDate;

    @JsonProperty("is_continue")
    private boolean isContinue;


    /**
     * Constructor.
     *
     * @param userId         user ID
     * @param companyName    company name
     * @param description    description
     * @param companyWebsite company website
     * @param startDate      start date
     * @param finishDate     finish date
     * @param isContinue     is continue
     * @param jobDefinition  job definition
     */
    public ExperienceUpdateDTO(UUID userId, String companyName, String description, String companyWebsite,
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


    /**
     * Getter for job definition.
     *
     * @return job definition
     */
    public String getJobDefinition()
    {
        return jobDefinition;
    }

    /**
     * Setter for job definition.
     *
     * @param jobDefinition job definition
     */
    public void setJobDefinition(String jobDefinition)
    {
        this.jobDefinition = jobDefinition;
    }


    /**
     * Getter for user ID.
     *
     * @return user ID
     */
    public UUID getUserId()
    {
        return userId;
    }


    /**
     * Setter for user ID.
     *
     * @param userId user ID
     */
    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }


    /**
     * Getter for company name.
     *
     * @return company name
     */
    public String getCompanyName()
    {
        return companyName;
    }

    /**
     * Setter for company name.
     *
     * @param companyName company name
     */
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }


    /**
     * Getter for description.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Setter for description.
     *
     * @param description description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Getter for company website.
     *
     * @return company website
     */
    public String getCompanyWebsite()
    {
        return companyWebsite;
    }


    /**
     * Setter for company website.
     *
     * @param companyWebsite company website
     */
    public void setCompanyWebsite(String companyWebsite)
    {
        this.companyWebsite = companyWebsite;
    }


    /**
     * Getter for start date.
     *
     * @return start date
     */
    public LocalDate getStartDate()
    {
        return startDate;
    }


    /**
     * Setter for start date.
     *
     * @param startDate start date
     */
    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }


    /**
     * Getter for finish date.
     *
     * @return finish date
     */
    public LocalDate getFinishDate()
    {
        return finishDate;
    }


    /**
     * Setter for finish date.
     *
     * @param finishDate finish date
     */
    public void setFinishDate(LocalDate finishDate)
    {
        this.finishDate = finishDate;
    }


    /**
     * Getter for is continue.
     *
     * @return is continue
     */
    public boolean isContinue()
    {
        return isContinue;
    }


    /**
     * Setter for is continue.
     *
     * @param aContinue is continue
     */
    public void setIsContinue(boolean aContinue)
    {
        isContinue = aContinue;
    }


    /**
     * Getter for experience ID.
     *
     * @return experience ID
     */
    public UUID getExperienceId()
    {
        return experienceId;
    }

    /**
     * Setter for experience ID.
     *
     * @param experienceId experience ID
     */
    public void setExperienceId(UUID experienceId)
    {
        this.experienceId = experienceId;
    }



}
