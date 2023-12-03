package callofproject.dev.authentication.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompanyDTO
{
    @JsonProperty("company_name")
    private String companyName;

    private String id;

    public CompanyDTO(String companyName, String id)
    {
        this.companyName = companyName;
        this.id = id;
    }

    public CompanyDTO()
    {
    }

    public String getCompanyName()
    {
        return companyName;
    }
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
