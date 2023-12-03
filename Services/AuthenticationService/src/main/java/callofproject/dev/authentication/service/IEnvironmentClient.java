package callofproject.dev.authentication.service;

import callofproject.dev.authentication.dto.client.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${environment.name}", url = "${environment.url}")
public interface IEnvironmentClient
{
    @PostMapping("save/university")
    UniversityDTO saveUniversity(@RequestBody UniversitySaveDTO universityDTO);

    @PostMapping("save/course")
    CourseDTO saveCourse(@RequestBody CourseSaveDTO organizationDTO);

    @PostMapping("save/company")
    CompanyDTO saveCompany(@RequestBody CompanySaveDTO companyDTO);

    @PostMapping("save/course-organization")
    CourseOrganizationDTO saveCourseOrganization(@RequestBody CourseOrganizationSaveDTO courseOrganizationSaveDTO);
}
