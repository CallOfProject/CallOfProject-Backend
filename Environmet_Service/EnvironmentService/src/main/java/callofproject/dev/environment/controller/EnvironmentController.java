package callofproject.dev.environment.controller;

import callofproject.dev.environment.service.EnvironmentService;
import callofproject.dev.repository.environment.dto.CompanyDTO;
import callofproject.dev.repository.environment.dto.CourseOrganizatorDTO;
import callofproject.dev.repository.environment.dto.UniversityDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    public ResponseEntity<Object> saveUniversity(@RequestBody UniversityDTO universityDTO)
    {
        return ok(m_environmentService.saveUniversity(universityDTO));
    }

    @PostMapping("save/course_organizator")
    public ResponseEntity<Object> saveCourseOrganizator(@RequestBody CourseOrganizatorDTO courseOrganizatorDTO)
    {
        return ok(m_environmentService.saveCourseOrganizator(courseOrganizatorDTO));
    }

    @PostMapping("save/company")
    public ResponseEntity<Object> saveCompany(@RequestBody CompanyDTO companyDTO)
    {
        return ok(m_environmentService.saveCompany(companyDTO));
    }

    @GetMapping("find/university/name")
    public ResponseEntity<Object> findUniversityByName(@Valid @NotEmpty @NotBlank(message = "name field cannot be empty") @RequestParam("name") String name)
    {
        return ok(m_environmentService.findUniversityByName(name));
    }

    @GetMapping("find/university/all")
    public ResponseEntity<Object> findAllUniversities()
    {
        return ok(m_environmentService.findAllUniversity());
    }


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


    @GetMapping("find/course-organizators/all/contains")
    public ResponseEntity<Object> findAllCourseOrganizatorsContains(@RequestParam("n") String name)
    {
        return ok(m_environmentService.findAllByCourseOrganizatorsNameContainingIgnoreCase(name));
    }
}