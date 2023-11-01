package callofproject.dev.environment.service;

import callofproject.dev.environment.mapper.ICompanyMapper;
import callofproject.dev.environment.mapper.ICourseOrganizatorMapper;
import callofproject.dev.environment.mapper.IUniversityMapper;
import callofproject.dev.repository.environment.BeanName;
import callofproject.dev.repository.environment.dal.EnvironmentServiceHelper;
import callofproject.dev.repository.environment.dto.*;
import callofproject.dev.repository.environment.entity.University;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

import static java.util.stream.StreamSupport.stream;

@Service("callofproject.dev.environment.service")
@Lazy
public class EnvironmentService
{
    private final EnvironmentServiceHelper m_serviceHelper;
    private final IUniversityMapper m_universityMapper;
    private final ICompanyMapper m_companyMapper;
    private final ICourseOrganizatorMapper m_courseOrganizatorMapper;

    public EnvironmentService(@Qualifier(BeanName.SERVICE_HELPER) EnvironmentServiceHelper serviceHelper,
                              IUniversityMapper universityMapper,
                              ICompanyMapper companyMapper,
                              ICourseOrganizatorMapper courseOrganizatorMapper)
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

    public CourseOrganizatorsDTO findAllCourseOrganizator()
    {
        var organizators = m_serviceHelper.findAllCourseOrganizator();
        return m_courseOrganizatorMapper.toCourseOrganizatorsDTO(stream(organizators.spliterator(), false)
                .map(m_courseOrganizatorMapper::toCourseOrganizatorDTO).toList());
    }

    public UniversitiesDTO findAllUniversity()
    {
        var universities = m_serviceHelper.findAllUniversity();
        return m_universityMapper.toUniversitiesDTO(stream(universities.spliterator(), false).map(m_universityMapper::toUniversityDTO).toList());
    }
    // ------------------------------------------------------------------------

    public UniversityDTO saveUniversity(UniversityDTO university)
    {
        return m_universityMapper.toUniversityDTO(m_serviceHelper.saveUniversity(m_universityMapper.toUniversity(university)));
    }

    public CompanyDTO saveCompany(CompanyDTO company)
    {
        return m_companyMapper.toCompanyDTO(m_serviceHelper.saveCompany(m_companyMapper.toCompany(company)));
    }

    public CourseOrganizatorDTO saveCourseOrganizator(CourseOrganizatorDTO organizator)
    {
        var org = m_serviceHelper.saveCourseOrganizator(m_courseOrganizatorMapper.toCourseOrganizator(organizator));
        return m_courseOrganizatorMapper.toCourseOrganizatorDTO(org);
    }

    // ------------------------------------------------------------------------
    public Optional<UniversityDTO> findUniversityByName(String universityName)
    {
        var university = m_serviceHelper.findUniversityByName(universityName);

        return university.map(m_universityMapper::toUniversityDTO);
    }

    public Optional<CourseOrganizatorDTO> findCourseOrganizatorByName(String organizator)
    {
        var org = m_serviceHelper.findCourseOrganizatorByName(organizator);

        return org.map(m_courseOrganizatorMapper::toCourseOrganizatorDTO);
    }

    public Optional<CompanyDTO> findCompanyByName(String companyName)
    {
        var company = m_serviceHelper.findCompanyByName(companyName);

        return company.map(m_companyMapper::toCompanyDTO);
    }

    // ------------------------------------------------------------------------
    public Optional<CompanyDTO> findCompanyById(long id)
    {
        var company = m_serviceHelper.findCompanyById(id);

        return company.map(m_companyMapper::toCompanyDTO);
    }

    public CompaniesDTO findCompanyByIds(Collection<Long> id)
    {
        var companies = m_serviceHelper.findCompanyByIds(id);

        return m_companyMapper.toCompaniesDTO(stream(companies.spliterator(), false).map(m_companyMapper::toCompanyDTO).toList());
    }


    public Optional<UniversityDTO> findUniversityById(long id)
    {
        var university = m_serviceHelper.findUniversityById(id);

        return university.map(m_universityMapper::toUniversityDTO);
    }

    public UniversitiesDTO findUniversityByIds(Collection<Long> ids)
    {
        var universities = m_serviceHelper.findUniversityByIds(ids);
        return m_universityMapper.toUniversitiesDTO(stream(universities.spliterator(), false).map(m_universityMapper::toUniversityDTO).toList());
    }


    public Optional<CourseOrganizatorDTO> findCourseOrganizatorById(long id)
    {
        var organizator = m_serviceHelper.findCourseOrganizatorById(id);

        return organizator.map(m_courseOrganizatorMapper::toCourseOrganizatorDTO);
    }

    public CourseOrganizatorsDTO findCourseOrganizatorByIds(Collection<Long> ids)
    {
        var organizators = m_serviceHelper.findCourseOrganizatorByIds(ids);

        return m_courseOrganizatorMapper.toCourseOrganizatorsDTO(stream(organizators.spliterator(), false)
                .map(m_courseOrganizatorMapper::toCourseOrganizatorDTO).toList());
    }


    public UniversitiesDTO findAllByUniversityNameContainingIgnoreCase(String name)
    {
        var universites = m_serviceHelper.findAllByUniversityNameContainingIgnoreCase(name);
        return m_universityMapper.toUniversitiesDTO(stream(universites.spliterator(), false).map(m_universityMapper::toUniversityDTO).toList());
    }
}
