package callofproject.dev.environment.mapper;

import callofproject.dev.environment.dto.CompaniesDTO;
import callofproject.dev.environment.dto.CompanyDTO;
import callofproject.dev.repository.environment.entity.Company;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(implementationName = "CompanyMapperImpl", componentModel = "spring")
public interface ICompanyMapper
{
    CompanyDTO toCompanyDTO(Company company);

    Company toCompany(CompanyDTO companyDTO);

    default CompaniesDTO toCompaniesDTO(List<CompanyDTO> companyDTOs)
    {
        return new CompaniesDTO(companyDTOs);
    }
}
