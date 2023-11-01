package callofproject.dev.repository.environment.dto;

public class CompanyDTO
{
    private String companyName;

    public CompanyDTO(String companyName)
    {
        this.companyName = companyName.toUpperCase();
    }

    public String getCompanyName()
    {
        return companyName;
    }
}
