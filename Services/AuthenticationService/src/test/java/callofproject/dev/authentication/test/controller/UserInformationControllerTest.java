package callofproject.dev.authentication.test.controller;

import callofproject.dev.authentication.controller.UserInformationController;
import callofproject.dev.authentication.dto.environments.CourseCreateDTO;
import callofproject.dev.authentication.dto.environments.EducationCreateDTO;
import callofproject.dev.authentication.dto.environments.ExperienceCreateDTO;
import callofproject.dev.authentication.dto.environments.LinkCreateDTO;
import callofproject.dev.authentication.service.userinformation.UserInformationService;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.library.exception.service.DataServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.UUID;

import static callofproject.dev.authentication.util.AuthenticationServiceBeanName.TEST_PROPERTIES_FILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
public class UserInformationControllerTest
{
    @Mock
    private UserInformationService userInformationService;

    @InjectMocks
    private UserInformationController userInformationController;

    private LinkCreateDTO linkDto = new LinkCreateDTO(UUID.randomUUID(), "LinkedIn", "https://www.linkedin.com/in/username");

    private ExperienceCreateDTO getExperienceUpsertDTO()
    {
        var userId1 = UUID.randomUUID();
        var companyName1 = "Company A";
        var description1 = "Description for Company A";
        var companyWebsite1 = "https://www.companyA.com";
        var startDate1 = LocalDate.of(2022, 1, 1);
        var finishDate1 = LocalDate.of(2022, 12, 31);
        var isContinue1 = false;
        var jobDefinition1 = "Job Definition for Company A";

        return new ExperienceCreateDTO(userId1, companyName1, description1,
                companyWebsite1, startDate1, finishDate1, isContinue1, jobDefinition1);
    }

    private EducationCreateDTO getEducationUpsertDTO()
    {
        var userId1 = UUID.randomUUID();
        var schoolName1 = "Sample School 1";
        var department1 = "Sample Department 1";
        var description1 = "Sample Description 1";
        var startDate1 = LocalDate.of(2022, 1, 1);
        var finishDate1 = LocalDate.of(2022, 12, 31);
        var isContinue1 = false;
        var gpa1 = 3.5;

        return new EducationCreateDTO(userId1, schoolName1, department1,
                description1, startDate1, finishDate1, isContinue1, gpa1);
    }

    private CourseCreateDTO getCourseUpsertDTO()
    {
        var userId = UUID.randomUUID();
        var organizator = "Sample Organizator";
        var courseName = "Sample Course";
        var startDate = LocalDate.of(2022, 1, 1);
        var finishDate = LocalDate.of(2022, 12, 31);
        var isContinue = false;
        var description = "Sample Description";

        return new CourseCreateDTO(userId, organizator, courseName, startDate, finishDate, isContinue, description);
    }


    @Test
    public void testSaveEducation_withValidData_shouldReturnSuccess()
    {
        // given
        var expectedResult = new ResponseMessage<Object>("Education saved successfully!", 200, true);
        when(userInformationService.saveEducation(any(EducationCreateDTO.class))).thenReturn(expectedResult);
        // Act
        ResponseEntity<Object> response = userInformationController.saveEducation(getEducationUpsertDTO());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSaveEducation_withServiceException_shouldReturnInternalServerError()
    {
        // given
        when(userInformationService.saveEducation(any(EducationCreateDTO.class))).thenThrow(new DataServiceException("Education cannot be upserted!"));
        // Act
        ResponseEntity<Object> response = userInformationController.saveEducation(getEducationUpsertDTO());
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    public void testSaveExperience_withValidData_shouldReturnSuccess()
    {
        // given
        var expectedResult = new ResponseMessage<Object>("Experience saved successfully!", 200, true);
        when(userInformationService.saveExperience(any(ExperienceCreateDTO.class))).thenReturn(expectedResult);
        // Act
        ResponseEntity<Object> response = userInformationController.saveExperience(getExperienceUpsertDTO());
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void testExperience_withServiceException_shouldReturnInternalServerError()
    {
        // given
        when(userInformationService.saveExperience(any(ExperienceCreateDTO.class))).thenThrow(new DataServiceException("Experience cannot be upserted!"));
        // Act
        ResponseEntity<Object> response = userInformationController.saveExperience(getExperienceUpsertDTO());
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSaveLink_withValidData_shouldReturnSuccess()
    {
        // given
        var expectedResult = new ResponseMessage<Object>("Link saved successfully!", 200, true);
        when(userInformationService.saveLink(any(LinkCreateDTO.class))).thenReturn(expectedResult);
        // Act
        ResponseEntity<Object> response = userInformationController.saveLink(linkDto);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testLink_withServiceException_shouldReturnInternalServerError()
    {
        // given
        when(userInformationService.saveLink(any(LinkCreateDTO.class))).thenThrow(new DataServiceException("Link cannot be upserted!"));
        // Act
        ResponseEntity<Object> response = userInformationController.saveLink(linkDto);
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSaveCourse_withValidData_shouldReturnSuccess()
    {
        // given
        var expectedResult = new ResponseMessage<Object>("course saved successfully!", 200, true);
        when(userInformationService.saveCourse(any(CourseCreateDTO.class))).thenReturn(expectedResult);
        // Act
        ResponseEntity<Object> response = userInformationController.saveCourse(getCourseUpsertDTO());
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCourse_withServiceException_shouldReturnInternalServerError()
    {
        // given
        when(userInformationService.saveCourse(any(CourseCreateDTO.class))).thenThrow(new DataServiceException("Course cannot be upserted!"));
        // Act
        ResponseEntity<Object> response = userInformationController.saveCourse(getCourseUpsertDTO());
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testRemoveEducation_withValidId_shouldReturnSuccess()
    {
        // given
        var userId = UUID.randomUUID();
        var educationId = UUID.randomUUID();
        var expectedResult = new ResponseMessage<Object>("Education removed successfully!", 200, true);
        when(userInformationService.removeEducation(userId, educationId)).thenReturn(expectedResult);
        // Act
        ResponseEntity<Object> response = userInformationController.removeEducation(userId, educationId);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void testRemoveExperience_withValidId_shouldReturnSuccess()
    {
        // given
        var userId = UUID.randomUUID();
        var experienceId = UUID.randomUUID();
        var expectedResult = new ResponseMessage<Object>("Experience removed successfully!", 200, true);
        when(userInformationService.removeExperience(userId, experienceId)).thenReturn(expectedResult);
        // Act
        ResponseEntity<Object> response = userInformationController.removeExperience(userId, experienceId);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRemoveLink_withValidId_shouldReturnSuccess()
    {
        // given
        var userId = UUID.randomUUID();
        var linkId = 123L;
        var expectedResult = new ResponseMessage<Object>("Link removed successfully!", 200, true);
        when(userInformationService.removeLink(userId, linkId)).thenReturn(expectedResult);
        // Act
        ResponseEntity<Object> response = userInformationController.removeLink(userId, linkId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRemoveCourse_withValidId_shouldReturnSuccess()
    {
        // given
        var userId = UUID.randomUUID();
        var courseId = UUID.randomUUID();
        var expectedResult = new ResponseMessage<Object>("Course removed successfully!", 200, true);
        when(userInformationService.removeCourse(userId, courseId)).thenReturn(expectedResult);
        // Act
        ResponseEntity<Object> response = userInformationController.removeCourse(userId, courseId);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
