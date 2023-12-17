package callofproject.dev.project.controller;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.project.entity.enums.*;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.nosql.entity.ProjectTag;
import callofproject.dev.project.dto.overview.ProjectOverviewDTO;
import callofproject.dev.project.service.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest
{
    @Mock
    private AdminService m_adminService;
    @InjectMocks
    private AdminController m_adminController;

    private ProjectOverviewDTO provideProjectOverviewDTO()
    {
        return new ProjectOverviewDTO(
                "123456",
                "path/to/image.jpg",
                "Sample Project",
                "This is a sample project summary.",
                "The aim of this project is to...",
                LocalDate.of(2023, 12, 31),
                LocalDate.of(2024, 12, 31),
                LocalDate.of(2023, 1, 1),
                10,
                "Technical requirements go here.",
                "Special requirements go here.",
                EProjectProfessionLevel.Expert,
                EDegree.BACHELOR,
                EProjectLevel.INTERMEDIATE,
                EInterviewType.TEST,
                "John Doe",
                EFeedbackTimeRange.ONE_MONTH,
                EProjectStatus.IN_PROGRESS,
                List.of(
                        new ProjectTag("Tag1", UUID.randomUUID()),
                        new ProjectTag("Tag2", UUID.randomUUID())
                )
        );
    }

    @Test
    void testCancelProject_withGivenProjectId_shouldReturnResponseMessage()
    {
        // Arrange
        var response = new ResponseMessage<Object>("Project is canceled!", Status.OK, provideProjectOverviewDTO());
        when(m_adminService.cancelProject(any(UUID.class))).thenReturn(response);
        // Act
        var result = m_adminController.cancelProject(UUID.randomUUID());
        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testCancelProject_withGivenInvalidProjectId_shouldReturnInternalServerError()
    {
        // Arrange
        when(m_adminService.cancelProject(any(UUID.class))).thenThrow(new DataServiceException("Project is not canceled!"));
        // Act
        var result = m_adminController.cancelProject(UUID.randomUUID());
        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void testFindAllProjects_withGivenValidPageNumber_shouldReturnSuccessCode()
    {
        // Arrange
        var page = 1;
        var response = new MultipleResponseMessagePageable<Object>(1, 1, 1, "Projects found successfully!", List.of(provideProjectOverviewDTO()));
        when(m_adminService.findAll(page)).thenReturn(response);
        // Act
        var result = m_adminController.findAll(page);
        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testFindAllProjects_withGivenInvalidPageNumber_shouldReturnInternalServerError()
    {
        // Arrange
        var page = 0;
        when(m_adminService.findAll(page)).thenThrow(new DataServiceException("Server down!"));
        // Act
        var result = m_adminController.findAll(page);
        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
}
