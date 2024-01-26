package callofproject.dev.environment.controller;

import callofproject.dev.environment.dto.CompanySaveDTO;
import callofproject.dev.environment.dto.CourseOrganizationSaveDTO;
import callofproject.dev.environment.dto.CourseSaveDTO;
import callofproject.dev.environment.dto.UniversitySaveDTO;
import callofproject.dev.environment.service.EnvironmentService;
import callofproject.dev.repository.environment.entity.Company;
import callofproject.dev.repository.environment.entity.Course;
import callofproject.dev.repository.environment.entity.CourseOrganization;
import callofproject.dev.repository.environment.entity.University;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;


/**
 * EnvironmentController
 * - This controller is responsible for the environment microservice.
 */
@RestController
@RequestMapping("environment")
public class EnvironmentController
{
    private final EnvironmentService m_environmentService;

    /**
     * Constructor
     *
     * @param environmentService environment service
     */
    public EnvironmentController(@Qualifier("callofproject.dev.environment.service") EnvironmentService environmentService)
    {
        m_environmentService = environmentService;
    }


    /**
     * Save university
     *
     * @param universityDTO university DTO
     * @return university
     */
    @PostMapping("save/university")
    public University saveUniversity(@RequestBody UniversitySaveDTO universityDTO)
    {
        return m_environmentService.saveUniversity(universityDTO);
    }


    /**
     * Save course
     *
     * @param courseSaveDTO course DTO
     * @return course
     */
    @PostMapping("save/course")
    public Course saveCourse(@RequestBody CourseSaveDTO courseSaveDTO)
    {
        return m_environmentService.saveCourse(courseSaveDTO);
    }


    /**
     * Save company
     *
     * @param companyDTO company DTO
     * @return company
     */
    @PostMapping("save/company")
    public Company saveCompany(@RequestBody CompanySaveDTO companyDTO)
    {
        return m_environmentService.saveCompany(companyDTO);
    }


    /**
     * Save course organization
     *
     * @param courseOrganizationSaveDTO course organization DTO
     * @return course organization
     */
    @PostMapping("save/course-organization")
    public CourseOrganization saveCourseOrganization(@RequestBody CourseOrganizationSaveDTO courseOrganizationSaveDTO)
    {
        return m_environmentService.saveCourseOrganization(courseOrganizationSaveDTO);
    }

    // ------------------------------------------------------------------------

    /**
     * Find university by name
     *
     * @param name university name
     * @return university
     */
    @GetMapping("find/university/name")
    public ResponseEntity<Object> findUniversityByName(@RequestParam("name") String name)
    {
        return ok(m_environmentService.findUniversityByName(name));
    }


    /**
     * Find company by name
     *
     * @param name company name
     * @return company
     */
    @GetMapping("find/company/name")
    public ResponseEntity<Object> findCompanyByName(@RequestParam("name") String name)
    {
        return ok(m_environmentService.findCompanyByName(name));
    }


    /**
     * Find course by name
     *
     * @param name course name
     * @return course
     */
    @GetMapping("find/course/name")
    public ResponseEntity<Object> findCourseByName(@RequestParam("name") String name)
    {
        return ok(m_environmentService.findCourseByName(name));
    }


    /**
     * Find course organization by name
     *
     * @param name course organization name
     * @return course organization
     */
    @GetMapping("find/course-organization/name")
    public ResponseEntity<Object> findCourseOrganizationByName(@RequestParam("name") String name)
    {
        return ok(m_environmentService.findCourseOrganizationByName(name));
    }

    // ------------------------------------------------------------------------

    /**
     * Find all universities contains name
     *
     * @return universities
     */
    @GetMapping("find/university/all/contains")
    public ResponseEntity<Object> findAllUniversitiesContains(@RequestParam("n") String name)
    {
        return ok(m_environmentService.findAllByUniversityNameContainingIgnoreCase(name));
    }


    /**
     * Find all companies contains name
     *
     * @return companies
     */
    @GetMapping("find/company/all/contains")
    public ResponseEntity<Object> findAllCompaniesContains(@RequestParam("n") String name)
    {
        return ok(m_environmentService.findAllByCompanyNameContainingIgnoreCase(name));
    }


    /**
     * Find all courses contains name
     *
     * @return courses
     */
    @GetMapping("find/course/all/contains")
    public ResponseEntity<Object> findAllCourseContains(@RequestParam("n") String name)
    {
        return ok(m_environmentService.findAllByCourseNameContainingIgnoreCase(name));
    }


    /**
     * Find all course organizations contains name
     *
     * @return course organizations
     */
    @GetMapping("find/course-organization/all/contains")
    public ResponseEntity<Object> findAllCourseOrganizationsContains(@RequestParam("n") String name)
    {
        return ok(m_environmentService.findAllByCourseOrganizationsNameContainingIgnoreCase(name));
    }
}