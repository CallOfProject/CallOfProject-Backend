package callofproject.dev.environment.mapper;

import callofproject.dev.environment.dto.CompaniesDTO;
import callofproject.dev.environment.dto.CompanyDTO;
import callofproject.dev.repository.environment.entity.Company;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * ICompanyMapper
 * - MapStruct interface for mapping Company and CompanyDTO
 */
@Mapper(implementationName = "CompanyMapperImpl", componentModel = "spring")
public interface ICompanyMapper
{
    /**
     * toCompanyDTO
     * - Map Company to CompanyDTO
     *
     * @param company Company
     * @return CompanyDTO
     */
    CompanyDTO toCompanyDTO(Company company);

    /**
     * toCompany
     * - Map CompanyDTO to Company
     *
     * @param companyDTO CompanyDTO
     * @return Company
     */

    Company toCompany(CompanyDTO companyDTO);


    /**
     * toCompaniesDTO
     * - Map CompanyDTOs to CompaniesDTO
     *
     * @param companyDTOs CompanyDTOs
     * @return CompaniesDTO
     */
    default CompaniesDTO toCompaniesDTO(List<CompanyDTO> companyDTOs)
    {
        return new CompaniesDTO(companyDTOs);
    }
}
