package callofproject.dev.authentication.service;

import callofproject.dev.authentication.dto.client.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Interface representing the Feign client service for interacting with the environment-related APIs.
 */
@FeignClient(name = "${environment.name}", url = "${environment.url}")
public interface IEnvironmentClientService
{
    /**
     * Saves a new university using the provided UniversitySaveDTO.
     *
     * @param universityDTO The UniversitySaveDTO object containing university data to be saved.
     * @return A UniversityDTO representing the saved university.
     */
    @PostMapping("save/university")
    UniversityDTO saveUniversity(@RequestBody UniversitySaveDTO universityDTO);

    /**
     * Saves a new course using the provided CourseSaveDTO.
     *
     * @param organizationDTO The CourseSaveDTO object containing course data to be saved.
     * @return A CourseDTO representing the saved course.
     */
    @PostMapping("save/course")
    CourseDTO saveCourse(@RequestBody CourseSaveDTO organizationDTO);

    /**
     * Saves a new company using the provided CompanySaveDTO.
     *
     * @param companyDTO The CompanySaveDTO object containing company data to be saved.
     * @return A CompanyDTO representing the saved company.
     */
    @PostMapping("save/company")
    CompanyDTO saveCompany(@RequestBody CompanySaveDTO companyDTO);

    /**
     * Saves a new course organization using the provided CourseOrganizationSaveDTO.
     *
     * @param courseOrganizationSaveDTO The CourseOrganizationSaveDTO object containing course organization data to be saved.
     * @return A CourseOrganizationDTO representing the saved course organization.
     */
    @PostMapping("save/course-organization")
    CourseOrganizationDTO saveCourseOrganization(@RequestBody CourseOrganizationSaveDTO courseOrganizationSaveDTO);
}