package callofproject.dev.repository.environment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompanyDTO
{
    @JsonProperty("company_name")
    private String companyName;

    public CompanyDTO(String companyName)
    {
        this.companyName = companyName.toUpperCase();
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
}
