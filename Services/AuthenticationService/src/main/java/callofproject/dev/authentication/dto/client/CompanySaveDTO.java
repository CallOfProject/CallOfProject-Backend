package callofproject.dev.authentication.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Data Transfer Object for a company.
 */
public class CompanySaveDTO
{
    @JsonProperty("company_name")
    private String companyName;


    /**
     * Constructor.
     *
     * @param companyName The company name.
     */
    public CompanySaveDTO(String companyName)
    {
        this.companyName = companyName.toUpperCase();
    }


    /**
     * Constructor.
     */
    public CompanySaveDTO()
    {
    }


    /**
     * Gets the company name.
     *
     * @return The company name.
     */
    @JsonProperty("company_name")
    public String getCompanyName()
    {
        return companyName;
    }


    /**
     * Sets the company name.
     *
     * @param companyName The company name.
     */
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }
}
