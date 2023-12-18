package callofproject.dev.project.controller;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.project.dto.ProjectSaveDTO;
import callofproject.dev.project.dto.ProjectUpdateDTO;
import callofproject.dev.project.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static callofproject.dev.data.common.status.Status.CREATED;
import static callofproject.dev.project.DataProvider.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest
{
    @Mock
    private ProjectService m_projectService;

    @InjectMocks
    private ProjectController m_projectController;


    @Test
    void testProjectCreate_withGivenProjectSaveDTO_shouldReturnAccepted()
    {
        var responseMessage = new ResponseMessage<Object>("Project Created Successfully!", CREATED, provideProjectOverviewDTO());
        //Arrange
        when(m_projectService.saveProject(any(ProjectSaveDTO.class))).thenReturn(responseMessage);
        //Act
        var response = m_projectController.save(provideProjectSaveDTO());
        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testProjectCreate_withGivenProjectSaveDTO_shouldReturnInternalServerError()
    {
        //Arrange
        when(m_projectService.saveProject(any(ProjectSaveDTO.class))).thenThrow(new DataServiceException("Project cannot be created!"));
        //Act
        var response = m_projectController.save(provideProjectSaveDTO());
        //Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testProjectUpdate_withGivenProjectUpdateDTO_shouldReturnAccepted()
    {
        //Arrange
        var responseMessage = new ResponseMessage<Object>("Project Updated Successfully!", CREATED, provideProjectOverviewDTO());
        when(m_projectService.updateProject(any(ProjectUpdateDTO.class))).thenReturn(responseMessage);
        //Act
        var response = m_projectController.updateProject(projectUpdateDTO());
        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testProjectUpdate_withGivenProjectUpdateDTO_shouldReturnInternalServerError()
    {
        //Arrange
        when(m_projectService.updateProject(any(ProjectUpdateDTO.class))).thenThrow(new DataServiceException("Project cannot be updated!"));
        //Act
        var response = m_projectController.updateProject(projectUpdateDTO());
        //Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testFindAllParticipantProjectByUserId_withGivenUserIdAndPage_shouldReturnAccepted()
    {
        //Arrange
        var responseMessage = new MultipleResponseMessagePageable<Object>(1, 1, 1, "Projects found successfully!", provideProjectOverviewDTO());
        when(m_projectService.findAllParticipantProjectByUserId(any(UUID.class), any(Integer.class))).thenReturn(responseMessage);
        //Act
        var response = m_projectController.findAllParticipantProjectByUserId(UUID.randomUUID().toString(), 1);
        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testFindAllParticipantProjectByUserId_withGivenInvalidUserIdAndPage_shouldReturnInternalServerError()
    {
        //Arrange
        when(m_projectService.findAllParticipantProjectByUserId(any(UUID.class), any(Integer.class))).thenThrow(new DataServiceException("Projects cannot be found!"));
        //Act
        var response = m_projectController.findAllParticipantProjectByUserId(UUID.randomUUID().toString(), 1);
        //Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    void testFindAllOwnerProjectsByUserId_withGivenUserIdAndPage_shouldReturnAccepted()
    {
        //Arrange
        var responseMessage = new MultipleResponseMessagePageable<Object>(1, 1, 1, "Projects found successfully!", provideProjectOverviewDTO());
        when(m_projectService.findAllOwnerProjectsByUserId(any(UUID.class), any(Integer.class))).thenReturn(responseMessage);
        //Act
        var response = m_projectController.findAllOwnerProjectsByUserId(UUID.randomUUID(), 1);
        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testFindAllOwnerProjectsByUserId_withGivenInvalidUserIdAndPage_shouldReturnInternalServerError()
    {
        //Arrange
        when(m_projectService.findAllOwnerProjectsByUserId(any(UUID.class), any(Integer.class))).thenThrow(new DataServiceException("Projects cannot be found!"));
        //Act
        var response = m_projectController.findAllOwnerProjectsByUserId(UUID.randomUUID(), 1);
        //Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    void testFindAllOwnerProjectsByUsername_withGivenUserIdAndPage_shouldReturnAccepted()
    {
        //Arrange
        var responseMessage = new MultipleResponseMessagePageable<Object>(1, 1, 1, "Projects found successfully!", provideProjectOverviewDTO());
        when(m_projectService.findAllOwnerProjectsByUsername(any(String.class), any(Integer.class))).thenReturn(responseMessage);
        //Act
        var response = m_projectController.findAllOwnerProjectsByUsername("username", 1);
        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testFindAllOwnerProjectsByUsername_withGivenInvalidUserIdAndPage_shouldReturnInternalServerError()
    {
        //Arrange
        when(m_projectService.findAllOwnerProjectsByUsername(any(String.class), any(Integer.class))).thenThrow(new DataServiceException("Projects cannot be found!"));
        //Act
        var response = m_projectController.findAllOwnerProjectsByUsername("username", 1);
        //Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testFindProjectOverview_withGivenProjectId_shouldReturnAccepted()
    {
        //Arrange
        var responseMessage = new ResponseMessage<Object>("Project found successfully!", CREATED, provideProjectOverviewDTO());
        when(m_projectService.findProjectOverview(any(UUID.class))).thenReturn(responseMessage);
        //Act
        var response = m_projectController.findProjectOverview(UUID.randomUUID());
        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }


    @Test
    void testFindProjectOverview_withGivenInvalidProjectId_shouldReturnInternalServerError()
    {
        //Arrange
        when(m_projectService.findProjectOverview(any(UUID.class))).thenThrow(new DataServiceException("Project cannot be found!"));
        //Act
        var response = m_projectController.findProjectOverview(UUID.randomUUID());
        //Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }


    @Test
    void testFindProjectDetail_withGivenProjectIdAndUserId_shouldReturnAccepted()
    {
        //Arrange
        var responseMessage = new ResponseMessage<Object>("Project found successfully!", CREATED, provideProjectOverviewDTO());
        when(m_projectService.findProjectOwnerView(any(UUID.class), any(UUID.class))).thenReturn(responseMessage);
        //Act
        var response = m_projectController.findProjectOwnerView(UUID.randomUUID(), UUID.randomUUID());
        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testFindProjectDetail_withGivenInvalidProjectIdAndUserId_shouldReturnInternalServerError()
    {
        //Arrange
        when(m_projectService.findProjectOwnerView(any(UUID.class), any(UUID.class))).thenThrow(new DataServiceException("Project cannot be found!"));
        //Act
        var response = m_projectController.findProjectOwnerView(UUID.randomUUID(), UUID.randomUUID());
        //Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testFindProjectDiscoveryViews_withGivenPageNumber_shouldReturnAccepted()
    {
        //Arrange
        var responseMessage = new MultipleResponseMessagePageable<Object>(1, 1, 1, "Projects found successfully!", provideProjectOverviewDTO());
        when(m_projectService.findAllProjectDiscoveryView(any(Integer.class))).thenReturn(responseMessage);
        //Act
        var response = m_projectController.findAllProjectDiscoveryView(1);
        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testFindProjectDiscoveryViews_withGivenInvalidPageNumber_shouldReturnInternalServerError()
    {
        //Arrange
        when(m_projectService.findAllProjectDiscoveryView(any(Integer.class))).thenThrow(new DataServiceException("Projects cannot be found!"));
        //Act
        var response = m_projectController.findAllProjectDiscoveryView(1);
        //Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testAddProjectJoinRequest_withGivenProjectIdAndUserId_shouldReturnAccepted()
    {
        //Arrange
        var responseMessage = new ResponseMessage<Object>("Project join request added successfully!", CREATED, provideProjectOverviewDTO());
        when(m_projectService.addProjectJoinRequest(any(UUID.class), any(UUID.class))).thenReturn(responseMessage);
        //Act
        var response = m_projectController.addProjectJoinRequest(UUID.randomUUID(), UUID.randomUUID());
        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void testAddProjectJoinRequest_withGivenInvalidProjectIdAndUserId_shouldReturnInternalServerError()
    {
        //Arrange
        when(m_projectService.addProjectJoinRequest(any(UUID.class), any(UUID.class))).thenThrow(new DataServiceException("Project join request cannot be added!"));
        //Act
        var response = m_projectController.addProjectJoinRequest(UUID.randomUUID(), UUID.randomUUID());
        //Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }
}
