package callofproject.dev.repository.environment.dto;

public record CompanyDTO(String companyName)
{
    public CompanyDTO(String companyName)
    {
        this.companyName = companyName.toUpperCase();
    }
}
