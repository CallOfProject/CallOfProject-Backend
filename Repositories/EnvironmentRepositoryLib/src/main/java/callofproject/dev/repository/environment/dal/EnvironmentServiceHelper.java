package callofproject.dev.repository.environment.dal;

import callofproject.dev.repository.environment.entity.Company;
import callofproject.dev.repository.environment.entity.CourseOrganizator;
import callofproject.dev.repository.environment.entity.University;
import callofproject.dev.repository.environment.repository.ICompanyRepository;
import callofproject.dev.repository.environment.repository.ICourseOrganizatorRepository;
import callofproject.dev.repository.environment.repository.IUniversityRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

import static callofproject.dev.repository.environment.BeanName.*;


@Component(SERVICE_HELPER)
@Lazy
public class EnvironmentServiceHelper
{
    private final ICompanyRepository m_companyRepository;
    private final ICourseOrganizatorRepository m_courseOrganizatorRepository;
    private final IUniversityRepository m_universityRepository;

    public EnvironmentServiceHelper(@Qualifier(COMPANY_REPOSITORY) ICompanyRepository companyRepository,
                                    @Qualifier(COURSE_REPOSITORY) ICourseOrganizatorRepository courseOrganizatorRepository,
                                    @Qualifier(UNIVERSITY_REPOSITORY) IUniversityRepository universityRepository)
    {
        m_companyRepository = companyRepository;
        m_courseOrganizatorRepository = courseOrganizatorRepository;
        m_universityRepository = universityRepository;
    }

    public Iterable<Company> findAllCompany()
    {
        return m_companyRepository.findAll();
    }

    public Iterable<CourseOrganizator> findAllCourseOrganizator()
    {
        return m_courseOrganizatorRepository.findAll();
    }

    public Iterable<University> findAllUniversity()
    {
        return m_universityRepository.findAll();
    }
    // ------------------------------------------------------------------------

    public University saveUniversity(University university)
    {
        return m_universityRepository.save(university);
    }

    public Company saveCompany(Company company)
    {
        return m_companyRepository.save(company);
    }

    public CourseOrganizator saveCourseOrganizator(CourseOrganizator organizator)
    {
        return m_courseOrganizatorRepository.save(organizator);
    }

    // ------------------------------------------------------------------------
    public Optional<University> findUniversityByName(String universityName)
    {
        return m_universityRepository.findByUniversityName(universityName);
    }

    public Optional<CourseOrganizator> findCourseOrganizatorByName(String organizator)
    {
        return m_courseOrganizatorRepository.findByOrganizatorName(organizator);
    }

    public Optional<Company> findCompanyByName(String companyName)
    {
        return m_companyRepository.findByCompanyName(companyName);
    }

    // ------------------------------------------------------------------------
    public Optional<Company> findCompanyById(long id)
    {
        return m_companyRepository.findById(id);
    }

    public Iterable<Company> findCompanyByIds(Collection<Long> id)
    {
        return m_companyRepository.findAllById(id);
    }


    public Optional<University> findUniversityById(long id)
    {
        return m_universityRepository.findById(id);
    }

    public Iterable<University> findUniversityByIds(Collection<Long> ids)
    {
        return m_universityRepository.findAllById(ids);
    }


    public Optional<CourseOrganizator> findCourseOrganizatorById(long id)
    {
        return m_courseOrganizatorRepository.findById(id);
    }

    public Iterable<CourseOrganizator> findCourseOrganizatorByIds(Collection<Long> ids)
    {
        return m_courseOrganizatorRepository.findAllById(ids);
    }

    public Iterable<University> findAllByUniversityNameContainingIgnoreCase(String name)
    {
        return m_universityRepository.findAllByUniversityNameContainingIgnoreCase(name);
    }

    public Iterable<Company> findAllByCompanyNameContainingIgnoreCase(String name)
    {
        return m_companyRepository.findAllByCompanyNameContainingIgnoreCase(name);
    }

    public Iterable<CourseOrganizator> findAllByCourseOrganizatorNameContainingIgnoreCase(String name)
    {
        return m_courseOrganizatorRepository.findAllByOrganizatorNameContainsIgnoreCase(name);
    }
}
