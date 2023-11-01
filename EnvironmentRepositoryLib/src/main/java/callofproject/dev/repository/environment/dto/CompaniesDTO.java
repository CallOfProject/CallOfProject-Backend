package callofproject.dev.repository.environment.dto;

import java.util.List;

public class CompaniesDTO
{

    private List<CompanyDTO> companies;

    public CompaniesDTO(List<CompanyDTO> companies)
    {
        this.companies = companies;
    }

    public List<CompanyDTO> getCompanies()
    {
        return companies;
    }
}
