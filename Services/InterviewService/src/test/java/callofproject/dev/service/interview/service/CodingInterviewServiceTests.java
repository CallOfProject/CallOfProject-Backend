package callofproject.dev.service.interview.service;


import callofproject.dev.data.interview.BeanName;
import callofproject.dev.data.interview.entity.CodingInterview;
import callofproject.dev.data.interview.entity.Project;
import callofproject.dev.data.interview.entity.ProjectParticipant;
import callofproject.dev.data.interview.entity.User;
import callofproject.dev.service.interview.DatabaseCleaner;
import callofproject.dev.service.interview.Injection;
import callofproject.dev.service.interview.mapper.ICodingInterviewMapper;
import callofproject.dev.service.interview.mapper.IProjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static callofproject.dev.service.interview.ServiceBeanName.SERVICE_BEAN_NAME;
import static callofproject.dev.service.interview.ServiceBeanName.TEST_PROPERTIES_FILE;
import static callofproject.dev.service.interview.Util.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@EntityScan(basePackages = BeanName.REPOSITORY_PACKAGE)
@ComponentScan(basePackages = {SERVICE_BEAN_NAME, BeanName.REPOSITORY_PACKAGE})
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
public class CodingInterviewServiceTests
{
    @Autowired
    private DatabaseCleaner m_databaseCleaner;

    @Autowired
    private ICodingInterviewMapper m_codingInterviewMapper;

    @Autowired
    private Injection m_injection;

    private User projectOwner;
    private User participant1;
    private User participant2;
    private Project project;
    private CodingInterview codingInterview;
    @Autowired
    private IProjectMapper m_projectMapper;

    @BeforeEach
    public void setUp()
    {
        projectOwner = m_injection.getUserRepository().save(createUser());
        participant1 = m_injection.getUserRepository().save(createUser());
        participant2 = m_injection.getUserRepository().save(createUser());

        m_injection.getParticipantRepository().save(new ProjectParticipant(UUID.randomUUID(), project, participant1, LocalDateTime.now()));
        m_injection.getParticipantRepository().save(new ProjectParticipant(UUID.randomUUID(), project, participant2, LocalDateTime.now()));

        var list = new ArrayList<String>();
        list.add(participant1.getUserId().toString());
        list.add(participant2.getUserId().toString());


        var interviewDTO = m_codingInterviewMapper.toCodingInterview(createCodingInterviewDTO(project.getProjectId(), list));

        codingInterview = m_injection.getCodingInterviewRepository().save(interviewDTO);
        codingInterview.setProject(project);
        codingInterview = m_injection.getCodingInterviewRepository().save(interviewDTO);
    }

    @Test
    public void createCodingInterview_withGivenInvalidData_shouldThrowException()
    {
        var interviewDTO = m_codingInterviewMapper.toCodingInterview(createInvalidCodingInterviewDTO(project.getProjectId(), null));

        assertThrows(DataIntegrityViolationException.class, () -> m_injection.getCodingInterviewRepository().save(interviewDTO));
    }

    @Test
    public void deleteCodingInterview_withGivenValidIds_shouldReturnCodingInterviewDTO()
    {
        System.out.println(project.getProjectId());
        var result = m_injection.getCodingInterviewService().deleteCodeInterview(projectOwner.getUserId(), codingInterview.getCodingInterviewId());

        assert result != null;
    }

    @Test
    public void deleteCodingInterview_withGivenInvalidIds_shouldThrowsException()
    {

    }


    @Test
    public void deleteCodingInterviewByProjectId_withGivenValidIds_shouldReturnCodingInterviewDTO()
    {

    }

    @Test
    public void deleteCodingInterviewByProjectId_withGivenInvalidIds_shouldThrowsException()
    {

    }

    @Test
    public void addParticipant_withGivenValidIds_shouldReturnCodingInterviewDTO()
    {

    }

    @Test
    public void addParticipant_withGivenInvalidIds_shouldReturnThrowsException()
    {

    }


    @Test
    public void removeParticipant_withGivenValidIds_shouldReturnCodingInterviewDTO()
    {

    }

    @Test
    public void removeParticipant_withGivenInvalidIds_shouldReturnThrowsException()
    {

    }


    @Test
    public void getParticipants_withGivenValidIds_shouldReturnListOfUserDTO()
    {

    }

    @Test
    public void getParticipants_withGivenInvalidIds_shouldReturnThrowsException()
    {

    }

    @Test
    public void getInterviewById_withGivenValidId_shouldReturnCodingInterviewDTO()
    {

    }

    @Test
    public void getInterviewById_withGivenInvalidId_shouldReturnThrowsException()
    {

    }

    @Test
    public void findUserInterviewInformation_withGivenValidUserId_shouldReturnOwnerProjectsDTO()
    {

    }

    @Test
    public void findUserInterviewInformation_withGivenInvalidUserId_shouldReturnThrowsException()
    {

    }


    @AfterEach
    public void tearDown()
    {
        m_databaseCleaner.clearH2Database();
    }
}