package callofproject.dev.environment.service;

import callofproject.dev.environment.dto.*;
import callofproject.dev.environment.mapper.ICompanyMapper;
import callofproject.dev.environment.mapper.ICourseMapper;
import callofproject.dev.environment.mapper.ICourseOrganizationMapper;
import callofproject.dev.environment.mapper.IUniversityMapper;
import callofproject.dev.repository.environment.BeanName;
import callofproject.dev.repository.environment.dal.EnvironmentServiceHelper;
import callofproject.dev.repository.environment.entity.Company;
import callofproject.dev.repository.environment.entity.Course;
import callofproject.dev.repository.environment.entity.CourseOrganization;
import callofproject.dev.repository.environment.entity.University;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Collection;
import java.util.Optional;

import static java.util.stream.StreamSupport.stream;


/**
 * EnvironmentService
 * - This service is responsible for the environment microservice.
 */
@Service("callofproject.dev.environment.service")
@Lazy
public class EnvironmentService
{
    private final EnvironmentServiceHelper m_serviceHelper;
    private final IUniversityMapper m_universityMapper;
    private final ICompanyMapper m_companyMapper;
    private final ICourseMapper m_courseMapper;
    private final ICourseOrganizationMapper m_courseOrganizatorMapper;

    /**
     * Constructor
     *
     * @param serviceHelper           service helper
     * @param universityMapper        university mapper
     * @param companyMapper           company mapper
     * @param courseMapper            course mapper
     * @param courseOrganizatorMapper course organizator mapper
     */
    public EnvironmentService(@Qualifier(BeanName.SERVICE_HELPER) EnvironmentServiceHelper serviceHelper,
                              IUniversityMapper universityMapper,
                              ICompanyMapper companyMapper,
                              ICourseMapper courseMapper, ICourseOrganizationMapper courseOrganizatorMapper)
    {
        m_serviceHelper = serviceHelper;
        m_universityMapper = universityMapper;
        m_companyMapper = companyMapper;
        m_courseMapper = courseMapper;
        m_courseOrganizatorMapper = courseOrganizatorMapper;
    }


    /**
     * Find all companies
     *
     * @return companies
     */
    public CompaniesDTO findAllCompany()
    {
        var companies = m_serviceHelper.findAllCompany();
        return m_companyMapper.toCompaniesDTO(stream(companies.spliterator(), false).map(m_companyMapper::toCompanyDTO).toList());
    }


    /**
     * Find all courses
     *
     * @return courses
     */
    public CoursesDTO findAllCourse()
    {
        var courses = m_serviceHelper.findAllCourses();

        return m_courseMapper.toCoursesDTO(stream(courses.spliterator(), false)
                .map(m_courseMapper::toCourseDTO).toList());
    }

    /**
     * Find all course organizations
     *
     * @return course organizations
     */
    public CourseOrganizationsDTO findAllCourseOrganizations()
    {
        var organizations = m_serviceHelper.findAllCourseOrganizations();
        return m_courseOrganizatorMapper.toCourseOrganizationsDTO(stream(organizations.spliterator(), false)
                .map(m_courseOrganizatorMapper::toCourseOrganizationDTO).toList());
    }


    /**
     * Find all universities
     *
     * @return universities
     */
    public UniversitiesDTO findAllUniversity()
    {
        var universities = m_serviceHelper.findAllUniversity();
        return m_universityMapper.toUniversitiesDTO(stream(universities.spliterator(), false).map(m_universityMapper::toUniversityDTO).toList());
    }
    // ------------------------------------------------------------------------


    /**
     * Convert string to upper case and remove special characters
     *
     * @param str string
     * @return converted string
     */
    private String convert(String str)
    {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        return str.replaceAll("[^\\p{ASCII}]", "").trim().toUpperCase().replaceAll("\\s+", "_");
    }


    /**
     * Save university
     *
     * @param university university DTO
     * @return university
     */
    public University saveUniversity(UniversitySaveDTO university)
    {
        var universityName = convert(university.getUniversityName());
        var universityOpt = m_serviceHelper.findByUniversityNameIgnoreCase(universityName);

        return universityOpt.orElseGet(() -> m_serviceHelper.saveUniversity(new University(universityName)));
    }


    /**
     * Save company
     *
     * @param company company DTO
     * @return company
     */
    public Company saveCompany(CompanySaveDTO company)
    {
        var companyName = convert(company.companyName());
        var companyOpt = m_serviceHelper.findByCompanyNameIgnoreCase(companyName);
        return companyOpt.orElseGet(() -> m_serviceHelper.saveCompany(new Company(companyName)));
    }

    /**
     * Save course organization
     *
     * @param courseOrganizationDTO course organization DTO
     * @return course organization
     */
    public CourseOrganization saveCourseOrganization(CourseOrganizationSaveDTO courseOrganizationDTO)
    {
        var courseOrganizationName = convert(courseOrganizationDTO.courseName());
        var courseOrganizationOpt = m_serviceHelper.findByOrganizationNameIgnoreCase(courseOrganizationName);
        return courseOrganizationOpt.orElseGet(() -> m_serviceHelper.saveCourseOrganization(new CourseOrganization(courseOrganizationName)));
    }

    /**
     * Save course
     *
     * @param courseSaveDTO course DTO
     * @return course
     */
    public Course saveCourse(CourseSaveDTO courseSaveDTO)
    {
        var courseName = convert(courseSaveDTO.getCourseName());
        var courseOpt = m_serviceHelper.findCourseByNameIgnoreCase(courseName);

        return courseOpt.orElseGet(() -> m_serviceHelper.saveCourse(new Course(courseName)));
    }

    // ------------------------------------------------------------------------

    /**
     * Find university by name
     *
     * @param universityName university name
     * @return university
     */
    public Optional<UniversityDTO> findUniversityByName(String universityName)
    {
        var university = m_serviceHelper.findByUniversityNameIgnoreCase(universityName);

        return university.map(m_universityMapper::toUniversityDTO);
    }


    /**
     * Find course by name
     *
     * @param courseName course name
     * @return course
     */
    public Optional<CourseDTO> findCourseByName(String courseName)
    {
        var course = m_serviceHelper.findCourseByNameIgnoreCase(courseName);

        return course.map(m_courseMapper::toCourseDTO);
    }


    /**
     * Find course organization by name
     *
     * @param courseOrganizationName course organization name
     * @return course organization
     */
    public Optional<CourseOrganizationDTO> findCourseOrganizationByName(String courseOrganizationName)
    {
        var courseOrganization = m_serviceHelper.findByOrganizationNameIgnoreCase(courseOrganizationName);

        return courseOrganization.map(m_courseOrganizatorMapper::toCourseOrganizationDTO);
    }


    /**
     * Find company by name
     *
     * @param companyName company name
     * @return company
     */
    public Optional<CompanyDTO> findCompanyByName(String companyName)
    {
        var company = m_serviceHelper.findByCompanyNameIgnoreCase(companyName);

        return company.map(m_companyMapper::toCompanyDTO);
    }

    // ------------------------------------------------------------------------

    /**
     * Find company by id
     *
     * @param id company id
     * @return company
     */
    public Optional<CompanyDTO> findCompanyById(String id)
    {
        var company = m_serviceHelper.findCompanyById(id);

        return company.map(m_companyMapper::toCompanyDTO);
    }


    /**
     * Find company by ids
     *
     * @param id company id
     * @return company
     */
    public CompaniesDTO findCompanyByIds(Collection<String> id)
    {
        var companies = m_serviceHelper.findCompanyByIds(id);

        return m_companyMapper.toCompaniesDTO(stream(companies.spliterator(), false).map(m_companyMapper::toCompanyDTO).toList());
    }


    /**
     * Find university by id
     *
     * @param id university id
     * @return university
     */
    public Optional<UniversityDTO> findUniversityById(String id)
    {
        var university = m_serviceHelper.findUniversityById(id);

        return university.map(m_universityMapper::toUniversityDTO);
    }


    /**
     * Find university by ids
     *
     * @param ids university ids
     * @return university
     */
    public UniversitiesDTO findUniversityByIds(Collection<String> ids)
    {
        var universities = m_serviceHelper.findUniversityByIds(ids);
        return m_universityMapper.toUniversitiesDTO(stream(universities.spliterator(), false).map(m_universityMapper::toUniversityDTO).toList());
    }


    /**
     * Find course by id
     *
     * @param id course id
     * @return course
     */
    public Optional<CourseDTO> findCourseById(String id)
    {
        var course = m_serviceHelper.findCourseById(id);

        return course.map(m_courseMapper::toCourseDTO);
    }


    /**
     * Find course by ids
     *
     * @param ids course ids
     * @return course
     */
    public CoursesDTO findCourseByIds(Collection<String> ids)
    {
        var courses = m_serviceHelper.findCourseByIds(ids);

        return m_courseMapper.toCoursesDTO(stream(courses.spliterator(), false)
                .map(m_courseMapper::toCourseDTO).toList());
    }


    /**
     * Find course organization by id
     *
     * @param id course organization id
     * @return course organization
     */
    public Optional<CourseOrganizationDTO> findCourseOrganizationById(String id)
    {
        var courseOrganization = m_serviceHelper.findCourseOrganizationById(id);

        return courseOrganization.map(m_courseOrganizatorMapper::toCourseOrganizationDTO);
    }


    /**
     * Find course organization by ids
     *
     * @param ids course organization ids
     * @return course organization
     */
    public CourseOrganizationsDTO findCourseOrganizationByIds(Collection<String> ids)
    {
        var courseOrganizations = m_serviceHelper.findCourseOrganizationByIds(ids);

        return m_courseOrganizatorMapper.toCourseOrganizationsDTO(stream(courseOrganizations.spliterator(), false)
                .map(m_courseOrganizatorMapper::toCourseOrganizationDTO).toList());
    }
    // ------------------------------------------------------------------------

    /**
     * Find all courses contains name ignore case
     *
     * @return courses
     */
    public CoursesDTO findAllByCourseNameContainingIgnoreCase(String name)
    {
        var courses = m_serviceHelper.findAllCoursesByCourseNameContainingIgnoreCase(name);
        return m_courseMapper.toCoursesDTO(stream(courses.spliterator(), false).map(m_courseMapper::toCourseDTO).toList());
    }


    /**
     * Find all universities contains name ignore case
     *
     * @return universities
     */
    public UniversitiesDTO findAllByUniversityNameContainingIgnoreCase(String name)
    {
        var universities = m_serviceHelper.findAllUniversitiesByUniversityNameContainingIgnoreCase(name);
        return m_universityMapper.toUniversitiesDTO(stream(universities.spliterator(), false)
                .map(m_universityMapper::toUniversityDTO).toList());
    }


    /**
     * Find all course organizations contains name ignore case
     *
     * @return course organizations
     */
    public CourseOrganizationsDTO findAllByCourseOrganizationsNameContainingIgnoreCase(String name)
    {
        var organizations = m_serviceHelper.findAllCourseOrganizationsByCourseNameContainingIgnoreCase(name);
        return m_courseOrganizatorMapper.toCourseOrganizationsDTO(stream(organizations.spliterator(), false)
                .map(m_courseOrganizatorMapper::toCourseOrganizationDTO).toList());
    }


    /**
     * Find all companies contains name ignore case
     *
     * @return companies
     */
    public CompaniesDTO findAllByCompanyNameContainingIgnoreCase(String name)
    {
        var companies = m_serviceHelper.findAllCompaniesByCompanyNameContainingIgnoreCase(name);
        return m_companyMapper.toCompaniesDTO(stream(companies.spliterator(), false).map(m_companyMapper::toCompanyDTO).toList());
    }
}
