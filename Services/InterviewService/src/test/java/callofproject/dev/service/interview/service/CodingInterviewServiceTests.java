package callofproject.dev.service.interview.service;


import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.interview.BeanName;
import callofproject.dev.data.interview.entity.CodingInterview;
import callofproject.dev.data.interview.entity.Project;
import callofproject.dev.data.interview.entity.ProjectParticipant;
import callofproject.dev.data.interview.entity.User;
import callofproject.dev.data.interview.entity.enums.AdminOperationStatus;
import callofproject.dev.data.interview.entity.enums.EProjectStatus;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.service.interview.DatabaseCleaner;
import callofproject.dev.service.interview.Injection;
import callofproject.dev.service.interview.mapper.ICodingInterviewMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static callofproject.dev.service.interview.ServiceBeanName.SERVICE_BEAN_NAME;
import static callofproject.dev.service.interview.ServiceBeanName.TEST_PROPERTIES_FILE;
import static callofproject.dev.service.interview.Util.*;
import static java.util.UUID.randomUUID;
import static java.util.stream.StreamSupport.stream;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@EntityScan(basePackages = BeanName.REPOSITORY_PACKAGE)
@ComponentScan(basePackages = {SERVICE_BEAN_NAME, BeanName.REPOSITORY_PACKAGE})
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
public class CodingInterviewServiceTests
{
    @Autowired
    private ICodingInterviewMapper m_codingInterviewMapper;

    @Autowired
    private Injection m_injection;

    @Autowired
    private DatabaseCleaner m_databaseCleaner;

    private User projectOwner;
    private User participant1;
    private User participant2;
    private User participant3;
    private Project m_project;
    private CodingInterview codingInterview;

    @BeforeEach
    public void setUp()
    {
        projectOwner = m_injection.getUserRepository().save(createUser("owner"));
        participant1 = m_injection.getUserRepository().save(createUser("participant1"));
        participant2 = m_injection.getUserRepository().save(createUser("participant2"));
        participant3 = m_injection.getUserRepository().save(createUser("participant3"));

        m_project = new Project(randomUUID(), "Test Project -" + ms_random.nextInt(100), projectOwner);

        m_project.addProjectParticipant(new ProjectParticipant(randomUUID(), m_project, participant1, LocalDateTime.now()));
        m_project.addProjectParticipant(new ProjectParticipant(randomUUID(), m_project, participant2, LocalDateTime.now()));
        m_project.addProjectParticipant(new ProjectParticipant(randomUUID(), m_project, participant3, LocalDateTime.now()));

        m_project.setProjectStatus(EProjectStatus.IN_PROGRESS);
        m_project.setAdminOperationStatus(AdminOperationStatus.ACTIVE);

        m_project = m_injection.getInterviewServiceHelper().createProject(m_project);
        var interview = createCodingInterviewDTO(m_project.getProjectId(), List.of(participant1.getUserId().toString(), participant2.getUserId().toString(), participant3.getUserId().toString()));
        codingInterview = m_injection.getCodingInterviewRepository().save(m_codingInterviewMapper.toCodingInterview(interview));
        m_project.setCodingInterview(codingInterview);
        m_project = m_injection.getProjectRepository().save(m_project);
    }


    @Test
    public void deleteCodingInterview_withGivenValidIds_shouldReturnRemovedCodingInterviewDTO()
    {
        var removedInterview = m_injection.getCodingInterviewService().deleteCodeInterview(projectOwner.getUserId(), codingInterview.getCodingInterviewId());

        assertEquals(Status.OK, removedInterview.getStatusCode());
        assertTrue(stream(m_injection.getInterviewServiceHelper().findAllInterviews().spliterator(), false).toList().isEmpty());
    }

    @Test
    public void deleteCodingInterview_withGivenInvalidIds_shouldThrowsException()
    {
        assertThrows(DataServiceException.class, () -> m_injection.getCodingInterviewService().deleteCodeInterview(projectOwner.getUserId(), randomUUID()));
    }


    @Test
    public void addParticipant_withGivenValidIds_shouldReturnCodingInterviewDTO()
    {
        var participant = m_injection.getUserRepository().save(createUser("addedParticipant"));

        var response = m_injection.getCodingInterviewService().addParticipant(codingInterview.getCodingInterviewId(), participant.getUserId());

        assertEquals(Status.OK, response.getStatusCode());
        var updatedInterview = m_injection.getCodingInterviewRepository().findById(codingInterview.getCodingInterviewId());
        assertTrue(updatedInterview.isPresent());
        assertEquals(1, updatedInterview.get().getCodingInterviews().size());
    }

    @Test
    public void addParticipant_withGivenInvalidIds_shouldReturnThrowsException()
    {
        assertThrows(DataServiceException.class, () -> m_injection.getCodingInterviewService().addParticipant(randomUUID(), randomUUID()));
    }


    @Test
    public void removeParticipant_withGivenValidIds_shouldReturnCodingInterviewDTO()
    {
        var response = m_injection.getCodingInterviewService().addParticipant(codingInterview.getCodingInterviewId(), participant1.getUserId());
        assertEquals(Status.OK, response.getStatusCode());
        var updatedInterview = m_injection.getCodingInterviewRepository().findById(codingInterview.getCodingInterviewId());
        assertTrue(updatedInterview.isPresent());
        assertEquals(1, updatedInterview.get().getCodingInterviews().size());

        var result = m_injection.getCodingInterviewService().removeParticipant(codingInterview.getCodingInterviewId(), participant1.getUserId());
        assertEquals(Status.OK, result.getStatusCode());
        updatedInterview = m_injection.getCodingInterviewRepository().findById(codingInterview.getCodingInterviewId());
        assertTrue(updatedInterview.isPresent());
        assertTrue(updatedInterview.get().getCodingInterviews().isEmpty());
    }

    @Test
    public void removeParticipant_withGivenInvalidIds_shouldReturnThrowsException()
    {
        var response = m_injection.getCodingInterviewService().addParticipant(codingInterview.getCodingInterviewId(), participant1.getUserId());
        assertEquals(Status.OK, response.getStatusCode());
        var updatedInterview = m_injection.getCodingInterviewRepository().findById(codingInterview.getCodingInterviewId());
        assertTrue(updatedInterview.isPresent());
        assertEquals(1, updatedInterview.get().getCodingInterviews().size());

        assertThrows(DataServiceException.class, () -> m_injection.getCodingInterviewService().removeParticipant(randomUUID(), randomUUID()));
    }


    @Test
    public void getParticipants_withGivenValidIds_shouldReturnListOfUserDTO()
    {
        var response = m_injection.getCodingInterviewService().addParticipant(codingInterview.getCodingInterviewId(), participant1.getUserId());
        response = m_injection.getCodingInterviewService().addParticipant(codingInterview.getCodingInterviewId(), participant2.getUserId());
        response = m_injection.getCodingInterviewService().addParticipant(codingInterview.getCodingInterviewId(), participant3.getUserId());
        assertEquals(Status.OK, response.getStatusCode());
        var participants = m_injection.getCodingInterviewService().getParticipants(codingInterview.getCodingInterviewId());

        assertEquals(3, participants.getItemCount());
    }

    @Test
    public void getParticipants_withGivenInvalidIds_shouldReturnThrowsException()
    {
        assertThrows(DataServiceException.class, () -> m_injection.getCodingInterviewService().getParticipants(randomUUID()));
    }

    @Test
    public void getInterviewById_withGivenValidId_shouldReturnCodingInterviewDTO()
    {
        var interview = m_injection.getCodingInterviewService().getInterview(codingInterview.getCodingInterviewId());
        assertNotNull(interview);
        assertEquals(Status.OK, interview.getStatusCode());
    }

    @Test
    public void getInterviewById_withGivenInvalidId_shouldReturnThrowsException()
    {
        assertThrows(DataServiceException.class, () -> m_injection.getCodingInterviewService().getInterview(randomUUID()));
    }

    @Test
    public void findUserInterviewInformation_withGivenValidUserId_shouldReturnOwnerProjectsDTO()
    {
        var ownerProjects = m_injection.getCodingInterviewService().findUserInterviewInformation(projectOwner.getUserId());
        assertNotNull(ownerProjects);
        assertEquals(1, ownerProjects.getItemCount());
    }

    @Test
    public void findUserInterviewInformation_withGivenInvalidUserId_shouldReturnThrowsException()
    {
        assertThrows(DataServiceException.class, () -> m_injection.getCodingInterviewService().findUserInterviewInformation(randomUUID()));
    }


    @AfterEach
    public void tearDown()
    {
        m_databaseCleaner.clearH2Database();
    }
}