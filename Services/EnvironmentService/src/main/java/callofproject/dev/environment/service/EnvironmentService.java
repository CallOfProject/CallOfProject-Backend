package callofproject.dev.environment.service;

import callofproject.dev.environment.mapper.ICompanyMapper;
import callofproject.dev.environment.mapper.ICourseOrganizationMapper;
import callofproject.dev.environment.mapper.IUniversityMapper;
import callofproject.dev.repository.environment.BeanName;
import callofproject.dev.repository.environment.dal.EnvironmentServiceHelper;
import callofproject.dev.repository.environment.dto.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

import static java.util.Locale.ENGLISH;
import static java.util.stream.StreamSupport.stream;

@Service("callofproject.dev.environment.service")
@Lazy
public class EnvironmentService
{
    private final EnvironmentServiceHelper m_serviceHelper;
    private final IUniversityMapper m_universityMapper;
    private final ICompanyMapper m_companyMapper;
    private final ICourseOrganizationMapper m_courseOrganizatorMapper;

    public EnvironmentService(@Qualifier(BeanName.SERVICE_HELPER) EnvironmentServiceHelper serviceHelper,
                              IUniversityMapper universityMapper,
                              ICompanyMapper companyMapper,
                              ICourseOrganizationMapper courseOrganizatorMapper)
    {
        m_serviceHelper = serviceHelper;
        m_universityMapper = universityMapper;
        m_companyMapper = companyMapper;
        m_courseOrganizatorMapper = courseOrganizatorMapper;
    }

    public CompaniesDTO findAllCompany()
    {
        var companies = m_serviceHelper.findAllCompany();
        return m_companyMapper.toCompaniesDTO(stream(companies.spliterator(), false).map(m_companyMapper::toCompanyDTO).toList());
    }

    public CourseOrganizationsDTO findAllCourseOrganizator()
    {
        var organizators = m_serviceHelper.findAllCourseOrganizator();

        return m_courseOrganizatorMapper.toCourseOrganizationsDTO(stream(organizators.spliterator(), false)
                .map(m_courseOrganizatorMapper::toCourseOrganizationDTO).toList());
    }

    public UniversitiesDTO findAllUniversity()
    {
        var universities = m_serviceHelper.findAllUniversity();
        return m_universityMapper.toUniversitiesDTO(stream(universities.spliterator(), false).map(m_universityMapper::toUniversityDTO).toList());
    }
    // ------------------------------------------------------------------------

    private String convert(String str)
    {
        return str.trim().toUpperCase(ENGLISH).replaceAll("\\s+", "_");
    }

    public UniversityDTO saveUniversity(UniversityDTO university)
    {
        university.setUniversityName(convert(university.getUniversityName()));
        return m_universityMapper.toUniversityDTO(m_serviceHelper.saveUniversity(m_universityMapper.toUniversity(university)));
    }

    public CompanyDTO saveCompany(CompanyDTO company)
    {
        company.setCompanyName(convert(company.getCompanyName()));
        return m_companyMapper.toCompanyDTO(m_serviceHelper.saveCompany(m_companyMapper.toCompany(company)));
    }

    public CourseOrganizationDTO saveCourseOrganizator(CourseOrganizationDTO organizator)
    {
        System.out.println(organizator.getCourseOrganizationName());
        organizator.setCourseOrganizationName(convert(organizator.getCourseOrganizationName()));
        var org = m_serviceHelper.saveCourseOrganizator(m_courseOrganizatorMapper.toCourseOrganization(organizator));
        return m_courseOrganizatorMapper.toCourseOrganizationDTO(org);
    }

    // ------------------------------------------------------------------------
    public Optional<UniversityDTO> findUniversityByName(String universityName)
    {
        var university = m_serviceHelper.findByUniversityNameIgnoreCase(universityName);

        return university.map(m_universityMapper::toUniversityDTO);
    }

    public Optional<CourseOrganizationDTO> findCourseOrganizatorByName(String organizator)
    {
        var org = m_serviceHelper.findByOrganizatorNameIgnoreCase(organizator);

        return org.map(m_courseOrganizatorMapper::toCourseOrganizationDTO);
    }

    public Optional<CompanyDTO> findCompanyByName(String companyName)
    {
        var company = m_serviceHelper.findByCompanyNameIgnoreCase(companyName);

        return company.map(m_companyMapper::toCompanyDTO);
    }

    // ------------------------------------------------------------------------
    public Optional<CompanyDTO> findCompanyById(String id)
    {
        var company = m_serviceHelper.findCompanyById(id);

        return company.map(m_companyMapper::toCompanyDTO);
    }

    public CompaniesDTO findCompanyByIds(Collection<String> id)
    {
        var companies = m_serviceHelper.findCompanyByIds(id);

        return m_companyMapper.toCompaniesDTO(stream(companies.spliterator(), false).map(m_companyMapper::toCompanyDTO).toList());
    }


    public Optional<UniversityDTO> findUniversityById(String id)
    {
        var university = m_serviceHelper.findUniversityById(id);

        return university.map(m_universityMapper::toUniversityDTO);
    }

    public UniversitiesDTO findUniversityByIds(Collection<String> ids)
    {
        var universities = m_serviceHelper.findUniversityByIds(ids);
        return m_universityMapper.toUniversitiesDTO(stream(universities.spliterator(), false).map(m_universityMapper::toUniversityDTO).toList());
    }


    public Optional<CourseOrganizationDTO> findCourseOrganizatorById(String id)
    {
        var organizator = m_serviceHelper.findCourseOrganizatorById(id);

        return organizator.map(m_courseOrganizatorMapper::toCourseOrganizationDTO);
    }

    public CourseOrganizationsDTO findCourseOrganizatorByIds(Collection<String> ids)
    {
        var organizators = m_serviceHelper.findCourseOrganizatorByIds(ids);

        return m_courseOrganizatorMapper.toCourseOrganizationsDTO(stream(organizators.spliterator(), false)
                .map(m_courseOrganizatorMapper::toCourseOrganizationDTO).toList());
    }


    public UniversitiesDTO findAllByUniversityNameContainingIgnoreCase(String name)
    {
        var universites = m_serviceHelper.findAllByUniversityNameContainingIgnoreCase(name);
        return m_universityMapper.toUniversitiesDTO(stream(universites.spliterator(), false)
                .map(m_universityMapper::toUniversityDTO).toList());
    }

    public CourseOrganizationsDTO findAllByCourseOrganizatorsNameContainingIgnoreCase(String name)
    {
        var organizators = m_serviceHelper.findAllByCourseOrganizatorNameContainingIgnoreCase(name);
        return m_courseOrganizatorMapper.toCourseOrganizationsDTO(stream(organizators.spliterator(), false)
                .map(m_courseOrganizatorMapper::toCourseOrganizationDTO).toList());
    }

    public CompaniesDTO findAllByCompanyNameContainingIgnoreCase(String name)
    {
        var companies = m_serviceHelper.findAllByCompanyNameContainingIgnoreCase(name);
        return m_companyMapper.toCompaniesDTO(stream(companies.spliterator(), false).map(m_companyMapper::toCompanyDTO).toList());
    }
}
