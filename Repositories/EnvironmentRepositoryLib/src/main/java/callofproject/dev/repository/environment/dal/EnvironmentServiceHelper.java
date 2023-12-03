package callofproject.dev.repository.environment.dal;

import callofproject.dev.repository.environment.entity.Company;
import callofproject.dev.repository.environment.entity.Course;
import callofproject.dev.repository.environment.entity.CourseOrganization;
import callofproject.dev.repository.environment.entity.University;
import callofproject.dev.repository.environment.repository.ICompanyRepository;
import callofproject.dev.repository.environment.repository.ICourseOrganizationRepository;
import callofproject.dev.repository.environment.repository.ICourseRepository;
import callofproject.dev.repository.environment.repository.IUniversityRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

import static callofproject.dev.repository.environment.BeanName.*;


@Component(SERVICE_HELPER)
@Lazy
public class EnvironmentServiceHelper
{
    private final ICompanyRepository m_companyRepository;
    private final ICourseRepository m_courseRepository;
    private final IUniversityRepository m_universityRepository;
    private final ICourseOrganizationRepository m_organizationRepository;

    public EnvironmentServiceHelper(@Qualifier(COMPANY_REPOSITORY) ICompanyRepository companyRepository,
                                    @Qualifier(COURSE_REPOSITORY) ICourseRepository courseRepository,
                                    @Qualifier(UNIVERSITY_REPOSITORY) IUniversityRepository universityRepository,
                                    @Qualifier(COURSE_ORGANIZATION_REPOSITORY) ICourseOrganizationRepository organizationRepository)
    {
        m_companyRepository = companyRepository;
        m_courseRepository = courseRepository;
        m_universityRepository = universityRepository;
        m_organizationRepository = organizationRepository;
    }

    public Iterable<Company> findAllCompany()
    {
        return m_companyRepository.findAll();
    }

    public Iterable<CourseOrganization> findAllCourseOrganizations()
    {
        return m_organizationRepository.findAll();
    }

    public Iterable<Course> findAllCourses()
    {
        return m_courseRepository.findAll();
    }

    public Iterable<University> findAllUniversity()
    {
        return m_universityRepository.findAll();
    }
    // ------------------------------------------------------------------------

    public University saveUniversity(University university)
    {
        var universityOpt = findByUniversityNameIgnoreCase(university.getUniversityName());

        return universityOpt.orElseGet(() -> m_universityRepository.save(university));

    }

    public CourseOrganization saveCourseOrganization(CourseOrganization courseOrganization)
    {
        var courseOrganizationOpt = m_organizationRepository.findByCourseOrganizationNameIgnoreCase(courseOrganization.getCourseOrganizationName());

        return courseOrganizationOpt.orElseGet(() -> m_organizationRepository.save(courseOrganization));

    }

    public Company saveCompany(Company company)
    {
        var companyOpt = findByCompanyNameIgnoreCase(company.getCompanyName());

        return companyOpt.orElseGet(() -> m_companyRepository.save(company));

    }

    public Course saveCourse(Course course)
    {
        var courseOpt = findCourseByNameIgnoreCase(course.getCourseName());

        return courseOpt.orElseGet(() -> m_courseRepository.save(course));

    }

    // ------------------------------------------------------------------------
    public Optional<University> findByUniversityNameIgnoreCase(String universityName)
    {
        return m_universityRepository.findByUniversityNameIgnoreCase(universityName.trim().toUpperCase(Locale.ENGLISH));
    }

    public Optional<CourseOrganization> findByOrganizationNameIgnoreCase(String organizationName)
    {
        return m_organizationRepository.findByCourseOrganizationNameIgnoreCase(organizationName.trim().toUpperCase(Locale.ENGLISH));
    }

    public Optional<Course> findCourseByNameIgnoreCase(String courseName)
    {
        return m_courseRepository.findByCourseNameIgnoreCase(courseName.trim().toUpperCase(Locale.ENGLISH));
    }

    public Optional<Company> findByCompanyNameIgnoreCase(String companyName)
    {
        return m_companyRepository.findByCompanyNameIgnoreCase(companyName.trim().toUpperCase(Locale.ENGLISH));
    }

    // ------------------------------------------------------------------------
    public Optional<Company> findCompanyById(String id)
    {
        return m_companyRepository.findById(id);
    }

    public Iterable<Company> findCompanyByIds(Collection<String> id)
    {
        return m_companyRepository.findAllById(id);
    }

    public Optional<CourseOrganization> findCourseOrganizationById(String id)
    {
        return m_organizationRepository.findById(id);
    }

    public Iterable<CourseOrganization> findCourseOrganizationByIds(Collection<String> id)
    {
        return m_organizationRepository.findAllById(id);
    }


    public Optional<University> findUniversityById(String id)
    {
        return m_universityRepository.findById(id);
    }

    public Iterable<University> findUniversityByIds(Collection<String> ids)
    {
        return m_universityRepository.findAllById(ids);
    }


    public Optional<Course> findCourseById(String id)
    {
        return m_courseRepository.findById(id);
    }

    public Iterable<Course> findCourseByIds(Collection<String> ids)
    {
        return m_courseRepository.findAllById(ids);
    }

    // ------------------------------------------------------------------------
    public Iterable<University> findAllUniversitiesByUniversityNameContainingIgnoreCase(String name)
    {
        return m_universityRepository.findAllByUniversityNameContainingIgnoreCase(name.trim());
    }

    public Iterable<Company> findAllCompaniesByCompanyNameContainingIgnoreCase(String name)
    {
        return m_companyRepository.findAllByCompanyNameContainingIgnoreCase(name.trim());
    }

    public Iterable<Course> findAllCoursesByCourseNameContainingIgnoreCase(String name)
    {
        return m_courseRepository.findAllByCourseNameContainsIgnoreCase(name.trim());
    }

    public Iterable<CourseOrganization> findAllCourseOrganizationsByCourseNameContainingIgnoreCase(String name)
    {
        return m_organizationRepository.findAllByCourseOrganizationNameContainsIgnoreCase(name.trim());
    }
}
