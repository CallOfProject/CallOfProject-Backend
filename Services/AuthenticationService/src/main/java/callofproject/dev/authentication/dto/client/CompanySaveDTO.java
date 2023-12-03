package callofproject.dev.authentication.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompanySaveDTO
{
    @JsonProperty("company_name")
    private String companyName;


    public CompanySaveDTO(String companyName)
    {
        this.companyName = companyName.toUpperCase();
    }

    public CompanySaveDTO()
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
