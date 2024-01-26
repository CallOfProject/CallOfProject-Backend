package callofproject.dev.environment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CompanyDTO
 */
public class CompanyDTO
{
    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("company_id")
    private String id;

    public CompanyDTO(String companyName, String id)
    {
        this.companyName = companyName;
        this.id = id;
    }

    public CompanyDTO()
    {
    }

    @JsonProperty("company_name")
    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    @JsonProperty("company_id")
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
