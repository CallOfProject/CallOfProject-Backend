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

@RestController
@RequestMapping("environment")
public class EnvironmentController
{
    private final EnvironmentService m_environmentService;

    public EnvironmentController(@Qualifier("callofproject.dev.environment.service") EnvironmentService environmentService)
    {
        m_environmentService = environmentService;
    }

    @PostMapping("save/university")
    public University saveUniversity(@RequestBody UniversitySaveDTO universityDTO)
    {
        return m_environmentService.saveUniversity(universityDTO);
    }

    @PostMapping("save/course")
    public Course saveCourse(@RequestBody CourseSaveDTO courseSaveDTO)
    {
        return m_environmentService.saveCourse(courseSaveDTO);
    }

    @PostMapping("save/company")
    public Company saveCompany(@RequestBody CompanySaveDTO companyDTO)
    {
        return m_environmentService.saveCompany(companyDTO);
    }

    @PostMapping("save/course-organization")
    public CourseOrganization saveCourseOrganization(@RequestBody CourseOrganizationSaveDTO courseOrganizationSaveDTO)
    {
        return m_environmentService.saveCourseOrganization(courseOrganizationSaveDTO);
    }

    // ------------------------------------------------------------------------
    @GetMapping("find/university/name")
    public ResponseEntity<Object> findUniversityByName(@RequestParam("name") String name)
    {
        return ok(m_environmentService.findUniversityByName(name));
    }

    @GetMapping("find/company/name")
    public ResponseEntity<Object> findCompanyByName(@RequestParam("name") String name)
    {
        return ok(m_environmentService.findCompanyByName(name));
    }

    @GetMapping("find/course/name")
    public ResponseEntity<Object> findCourseByName(@RequestParam("name") String name)
    {
        return ok(m_environmentService.findCourseByName(name));
    }

    @GetMapping("find/course-organization/name")
    public ResponseEntity<Object> findCourseOrganizationByName(@RequestParam("name") String name)
    {
        return ok(m_environmentService.findCourseOrganizationByName(name));
    }

    // ------------------------------------------------------------------------
    @GetMapping("find/university/all/contains")
    public ResponseEntity<Object> findAllUniversitiesContains(@RequestParam("n") String name)
    {
        return ok(m_environmentService.findAllByUniversityNameContainingIgnoreCase(name));
    }

    @GetMapping("find/company/all/contains")
    public ResponseEntity<Object> findAllCompaniesContains(@RequestParam("n") String name)
    {
        return ok(m_environmentService.findAllByCompanyNameContainingIgnoreCase(name));
    }


    @GetMapping("find/course/all/contains")
    public ResponseEntity<Object> findAllCourseContains(@RequestParam("n") String name)
    {
        return ok(m_environmentService.findAllByCourseNameContainingIgnoreCase(name));
    }

    @GetMapping("find/course-organization/all/contains")
    public ResponseEntity<Object> findAllCourseOrganizationsContains(@RequestParam("n") String name)
    {
        return ok(m_environmentService.findAllByCourseOrganizationsNameContainingIgnoreCase(name));
    }
}