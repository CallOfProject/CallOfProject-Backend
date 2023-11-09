package callofproject.dev.environment.mapper;

import callofproject.dev.repository.environment.dto.CompanyDTO;
import callofproject.dev.repository.environment.entity.Company;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-02T11:36:40+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class CompanyMapperImpl implements ICompanyMapper {

    @Override
    public CompanyDTO toCompanyDTO(Company company) {
        if ( company == null ) {
            return null;
        }

        String companyName = null;

        companyName = company.getCompanyName();

        CompanyDTO companyDTO = new CompanyDTO( companyName );

        return companyDTO;
    }

    @Override
    public Company toCompany(CompanyDTO companyDTO) {
        if ( companyDTO == null ) {
            return null;
        }

        Company company = new Company();

        company.setCompanyName( companyDTO.getCompanyName() );

        return company;
    }
}
