package callofproject.dev.authentication.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Data Transfer Object for a company.
 */
public class CompanyDTO
{
    @JsonProperty("company_name")
    private String companyName;

    private String id;


    /**
     * Constructor.
     *
     * @param companyName The company name.
     * @param id          The company id.
     */
    public CompanyDTO(String companyName, String id)
    {
        this.companyName = companyName;
        this.id = id;
    }


    /**
     * Constructor.
     */
    public CompanyDTO()
    {
    }


    /**
     * Gets the company name.
     *
     * @return The company name.
     */
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


    /**
     * Gets the company id.
     *
     * @return The company id.
     */
    public String getId()
    {
        return id;
    }


    /**
     * Sets the company id.
     *
     * @param id The company id.
     */
    public void setId(String id)
    {
        this.id = id;
    }
}
