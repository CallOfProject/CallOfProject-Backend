package callofproject.dev.authentication.test.service;

import callofproject.dev.authentication.DatabaseCleaner;
import callofproject.dev.authentication.Injection;
import callofproject.dev.authentication.dto.UserSignUpRequestDTO;
import callofproject.dev.authentication.dto.environments.CourseUpsertDTO;
import callofproject.dev.authentication.dto.environments.EducationUpsertDTO;
import callofproject.dev.authentication.dto.environments.ExperienceUpsertDTO;
import callofproject.dev.authentication.dto.environments.LinkUpsertDTO;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.repository.authentication.entity.Course;
import callofproject.dev.repository.authentication.entity.Education;
import callofproject.dev.repository.authentication.entity.Experience;
import callofproject.dev.repository.authentication.entity.Link;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.UUID;

import static callofproject.dev.authentication.util.AuthenticationServiceBeanName.*;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EntityScan(basePackages = REPO_PACKAGE)
@ComponentScan(basePackages = {BASE_PACKAGE, REPO_PACKAGE})
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
public class UserInformationTest
{
    @Autowired
    private DatabaseCleaner m_databaseCleaner;

    @Autowired
    private Injection m_injection;

    @Autowired
    private PasswordEncoder m_passwordEncoder;

    private Link link1;
    private Link link2;
    private Education education1;
    private Education education2;
    private Experience experience1;
    private Experience experience2;
    private Course course1;
    private Course course2;
    private UUID user1Id;
    private UUID user2Id;
    private UUID rootId;
    private UUID adminId;

    @BeforeEach
    public void setUpAndCheckUsers()
    {
        var user1 = new UserSignUpRequestDTO(
                "can@example.com",
                "Nuri",
                "Can",
                "OZTURK",
                "canozturk",
                "password123",
                LocalDate.of(1990, 5, 15)
        );
        var user2 = new UserSignUpRequestDTO(
                "halil@example.com",
                "Halil",
                "Can",
                "OZTURK",
                "halil",
                "securePassword",
                LocalDate.of(1985, 9, 8)
        );

        var savedUser1 = m_injection.getUserManagementService().saveUserCallback(user1);
        var savedUser2 = m_injection.getUserManagementService().saveUserCallback(user2);


        link1 = (Link) m_injection.getUserInformationService().upsertLink(new LinkUpsertDTO(savedUser1.getObject().userId(), "Link Title", "http://link.com")).getObject();
        link2 = (Link) m_injection.getUserInformationService().upsertLink(new LinkUpsertDTO(savedUser2.getObject().userId(), "Link Title", "http://link.com")).getObject();


        education1 = (Education) m_injection.getUserInformationService().upsertEducation(new EducationUpsertDTO(savedUser1.getObject().userId(), "YASAR UNIVERSITY",
                "ENGINEERING", "SOFTWARE ENGINEERING", LocalDate.now(), LocalDate.now().plusYears(4), true, 3.5)).getObject();
        education2 = (Education) m_injection.getUserInformationService().upsertEducation(new EducationUpsertDTO(savedUser2.getObject().userId(),
                "EGE UNIVERSITY", "ENGINEERING", "SOFTWARE ENGINEERING", LocalDate.now(), LocalDate.now().plusYears(4), true, 2.5)).getObject();

        experience1 = (Experience) m_injection.getUserInformationService().upsertExperience(new ExperienceUpsertDTO(savedUser1.getObject().userId(), "Company Name", "Description", "http://website.com", LocalDate.now(), LocalDate.now().plusYears(2), true, "Job Definition")).getObject();
        experience2 = (Experience) m_injection.getUserInformationService().upsertExperience(new ExperienceUpsertDTO(savedUser2.getObject().userId(), "Company Name", "Description", "http://website.com", LocalDate.now(), LocalDate.now().plusYears(2), true, "Job Definition")).getObject();


        course1 = (Course) m_injection.getUserInformationService().upsertCourse(new CourseUpsertDTO(savedUser1.getObject().userId(), "Organizator", "Course Name", LocalDate.now(), LocalDate.now().plusMonths(6), false, "Description")).getObject();
        course2 = (Course) m_injection.getUserInformationService().upsertCourse(new CourseUpsertDTO(savedUser2.getObject().userId(), "Organizator", "Course Name", LocalDate.now(), LocalDate.now().plusMonths(6), false, "Description")).getObject();

        assertNotNull(savedUser1);
        assertNotNull(savedUser2);
        assertNotNull(link1);
        assertNotNull(link2);
        assertNotNull(education1);
        assertNotNull(education2);
        assertNotNull(experience1);
        assertNotNull(experience2);
        assertNotNull(course1);
        assertNotNull(course2);

        user1Id = savedUser1.getObject().userId();
        user2Id = savedUser2.getObject().userId();
    }

    @Test
    public void testUpsertEducation_withValidData_shouldReturnSuccessResponse()
    {
        EducationUpsertDTO dto = new EducationUpsertDTO(user2Id, "DOKUZ EYLUL UNIVERSITY", "Department", "Description", LocalDate.now(), LocalDate.now().plusYears(4), true, 3.5);
        var response = m_injection.getUserInformationService().upsertEducation(dto);
        assertNotNull(response);
    }

    @Test
    public void testUpsertEducation_withInvalidData_shouldThrowException()
    {
        EducationUpsertDTO dto = new EducationUpsertDTO(UUID.randomUUID(), "YASAR UNIVERSITY",
                "ENGINEERING", "SOFTWARE ENGINEERING", null, null, true, -1);
        assertThrows(DataServiceException.class, () -> m_injection.getUserInformationService().upsertEducation(dto));
    }

    @Test
    public void testUpsertExperience_withValidData_shouldReturnSuccessResponse()
    {
        ExperienceUpsertDTO dto = new ExperienceUpsertDTO(user1Id, "Company Name", "Description", "http://website.com", LocalDate.now(), LocalDate.now().plusYears(2), true, "Job Definition");
        var response = m_injection.getUserInformationService().upsertExperience(dto);
        assertNotNull(response);
    }

    @Test
    public void testUpsertExperience_withInvalidData_shouldThrowException()
    {
        ExperienceUpsertDTO dto = new ExperienceUpsertDTO(user2Id, "", "", "", null, null, false, ""); // Invalid data
        assertThrows(DataServiceException.class, () -> m_injection.getUserInformationService().upsertExperience(dto));
    }

    @Test
    public void testUpsertCourse_withValidData_shouldReturnSuccessResponse()
    {
        CourseUpsertDTO dto = new CourseUpsertDTO(user1Id, "Organizator", "Course Name", LocalDate.now(), LocalDate.now().plusMonths(6), false, "Description");
        var response = m_injection.getUserInformationService().upsertCourse(dto);
        assertNotNull(response);
    }

    @Test
    public void testUpsertCourse_withInvalidData_shouldThrowException()
    {
        CourseUpsertDTO dto = new CourseUpsertDTO(user2Id, "", "", null, null, false, ""); // Invalid data
        assertThrows(DataServiceException.class, () -> m_injection.getUserInformationService().upsertCourse(dto));
    }

    @Test
    public void testUpsertLink_withValidData_shouldReturnSuccessResponse()
    {
        LinkUpsertDTO dto = new LinkUpsertDTO(user1Id, "Link Title", "http://link.com");
        var response = m_injection.getUserInformationService().upsertLink(dto);
        assertNotNull(response);
    }

    @Test
    public void testUpsertLink_withInvalidData_shouldThrowException()
    {
        LinkUpsertDTO dto = new LinkUpsertDTO(user1Id, null, null); // Invalid data
        assertThrows(DataServiceException.class, () -> m_injection.getUserInformationService().upsertLink(dto));
    }

    @Test
    public void testRemoveEducation_withValidId_shouldReturnSuccessResponse()
    {
        var response = m_injection.getUserInformationService().removeEducation(user1Id, education1.getEducation_id());
        assertNotNull(response);
    }




    @Test
    public void testRemoveEducation_withInvalidId_shouldReturnErrorResponse()
    {
        UUID invalidEducationId = UUID.randomUUID();
        assertThrows(DataServiceException.class, () -> m_injection.getUserInformationService().removeEducation(user2Id, invalidEducationId));
    }

    @Test
    public void testRemoveCourse_withValidId_shouldReturnSuccessResponse()
    {
        var response = m_injection.getUserInformationService().removeCourse(user1Id, course1.getCourse_id());
        assertNotNull(response);
    }

    @Test
    public void testRemoveCourse_withInvalidId_shouldReturnErrorResponse()
    {
        UUID invalidCourseId = UUID.randomUUID();
        assertThrows(DataServiceException.class, () -> m_injection.getUserInformationService().removeCourse(user2Id, invalidCourseId));
    }

    @Test
    public void testRemoveExperience_withValidId_shouldReturnSuccessResponse()
    {
        var response = m_injection.getUserInformationService().removeExperience(user2Id, experience2.getExperience_id());
        assertNotNull(response);
    }

    @Test
    public void testRemoveExperience_withInvalidId_shouldReturnErrorResponse()
    {
        UUID invalidExperienceId = UUID.randomUUID();
        assertThrows(DataServiceException.class, () -> m_injection.getUserInformationService().removeExperience(user2Id, invalidExperienceId));
    }

    @Test
    public void testRemoveLink_withValidId_shouldReturnSuccessResponse()
    {
        var response = m_injection.getUserInformationService().removeLink(user1Id, link1.getLink_id());
        assertNotNull(response);
    }

    @Test
    public void testRemoveLink_withInvalidId_shouldReturnErrorResponse()
    {
        long invalidLinkId = -1L;
        assertThrows(DataServiceException.class, () -> m_injection.getUserInformationService().removeLink(user2Id, invalidLinkId));
    }


    @AfterEach
    public void tearDown()
    {
        System.out.println("cleaning up");
        m_databaseCleaner.clearH2Database();
    }

}
