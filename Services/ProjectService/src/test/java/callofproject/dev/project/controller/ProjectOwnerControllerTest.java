package callofproject.dev.project.controller;

import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.project.entity.enums.EProjectStatus;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.project.dto.ParticipantRequestDTO;
import callofproject.dev.project.dto.SaveProjectParticipantDTO;
import callofproject.dev.project.service.ProjectOwnerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static callofproject.dev.data.common.status.Status.OK;
import static callofproject.dev.project.DataProvider.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectOwnerControllerTest
{
    @Mock
    private ProjectOwnerService m_projectOwnerService;

    @InjectMocks
    private ProjectOwnerController m_projectOwnerController;

    @Test
    public void testAddParticipant_withGivenSaveProjectParticipantDTO_shouldReturnAccepted()
    {
        //Arrange
        var response = new ResponseMessage<Boolean>("Participant added to project!", OK, true);
        when(m_projectOwnerService.addParticipant(any(SaveProjectParticipantDTO.class))).thenReturn(response);
        //Act
        var result = m_projectOwnerController.addParticipant(provideSaveProjectParticipantDTO());
        //Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testAddParticipant_withGivenSaveProjectParticipantDTO_shouldReturnInternalServerError()
    {
        //Arrange
        when(m_projectOwnerService.addParticipant(any(SaveProjectParticipantDTO.class)))
                .thenThrow(new DataServiceException("Participant could not be added to project!"));
        //Act
        var result = m_projectOwnerController.addParticipant(provideSaveProjectParticipantDTO());
        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }


    @Test
    public void testRemoveParticipant_withGivenProjectIdAndUserId_shouldReturnAccepted()
    {
        //Arrange
        var response = new ResponseMessage<Object>("Participant removed from project!", OK, true);
        when(m_projectOwnerService.removeParticipant(any(UUID.class), any(UUID.class))).thenReturn(response);
        //Act
        var result = m_projectOwnerController.removeParticipant(UUID.randomUUID(), UUID.randomUUID());
        //Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }


    @Test
    public void testRemoveParticipant_withGivenProjectIdAndUserId_shouldReturnInternalServerError()
    {
        //Arrange
        when(m_projectOwnerService.removeParticipant(any(UUID.class), any(UUID.class)))
                .thenThrow(new DataServiceException("Participant could not be removed from project!"));
        //Act
        var result = m_projectOwnerController.removeParticipant(UUID.randomUUID(), UUID.randomUUID());
        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }


    @Test
    public void testApproveParticipantRequest_withGivenRequestId_shouldReturnAccepted()
    {
        //Arrange
        var response = new ResponseMessage<Object>("Participant request approved!", OK, true);
        when(m_projectOwnerService.approveParticipantRequest(any(ParticipantRequestDTO.class))).thenReturn(response);
        //Act
        var result = m_projectOwnerController.approveProjectParticipantRequest(provideApproveParticipantRequestDTO());
        //Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }


    @Test
    public void testRejectParticipantRequest_withGivenRequestId_shouldReturnAccepted()
    {
        //Arrange
        var response = new ResponseMessage<Object>("Participant request rejected!", OK, true);
        when(m_projectOwnerService.approveParticipantRequest(any(ParticipantRequestDTO.class))).thenReturn(response);
        //Act
        var result = m_projectOwnerController.approveProjectParticipantRequest(provideRejectParticipantRequestDTO());
        //Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }


    @Test
    public void testApproveParticipantRequest_withGivenRequestId_shouldReturnInternalServerError()
    {
        //Arrange
        when(m_projectOwnerService.approveParticipantRequest(any(ParticipantRequestDTO.class)))
                .thenThrow(new DataServiceException("Participant request could not be approved!"));
        //Act
        var result = m_projectOwnerController.approveProjectParticipantRequest(provideApproveParticipantRequestDTO());
        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testFinishProject_withGivenValidOwner_shouldReturnAccepted()
    {
        //Arrange
        var response = new ResponseMessage<Object>("Project finished successfully!", OK, true);
        when(m_projectOwnerService.finishProject(any(UUID.class), any(UUID.class))).thenReturn(response);
        //Act
        var result = m_projectOwnerController.finishProject(UUID.randomUUID(), UUID.randomUUID());
        //Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testFinishProject_withGivenInvalidOwner_shouldReturnInternalServerError()
    {
        //Arrange
        when(m_projectOwnerService.finishProject(any(UUID.class), any(UUID.class)))
                .thenThrow(new DataServiceException("Project could not be finished!"));
        //Act
        var result = m_projectOwnerController.finishProject(UUID.randomUUID(), UUID.randomUUID());
        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testChangeProjectStatus_withGivenValidOwnerAndProjectId_shouldReturnAccepted()
    {
        //Arrange
        var response = new ResponseMessage<Object>("Project status changed successfully!", OK, true);
        when(m_projectOwnerService.changeProjectStatus(any(UUID.class), any(UUID.class), any(EProjectStatus.class))).thenReturn(response);
        //Act
        var result = m_projectOwnerController.changeProjectStatus(UUID.randomUUID(), UUID.randomUUID(), EProjectStatus.FINISHED);
        //Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }


    @Test
    public void testChangeProjectStatus_withGivenValidOwnerAndProjectId_shouldReturnInternalServerError()
    {
        //Arrange
        when(m_projectOwnerService.changeProjectStatus(any(UUID.class), any(UUID.class), any(EProjectStatus.class)))
                .thenThrow(new DataServiceException("Project status could not be changed!"));
        //Act
        var result = m_projectOwnerController.changeProjectStatus(UUID.randomUUID(), UUID.randomUUID(), EProjectStatus.FINISHED);
        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
}
