package callofproject.dev.service.interview.service;

import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.interview.BeanName;
import callofproject.dev.data.interview.entity.*;
import callofproject.dev.data.interview.entity.enums.AdminOperationStatus;
import callofproject.dev.data.interview.entity.enums.EProjectStatus;
import callofproject.dev.service.interview.DatabaseCleaner;
import callofproject.dev.service.interview.Injection;
import callofproject.dev.service.interview.dto.UserCodingInterviewDTO;
import callofproject.dev.service.interview.dto.UserTestInterviewDTO;
import callofproject.dev.service.interview.dto.test.UserCodingInterviewDTOV2;
import callofproject.dev.service.interview.mapper.ICodingInterviewMapper;
import callofproject.dev.service.interview.mapper.ITestInterviewMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static callofproject.dev.service.interview.ServiceBeanName.SERVICE_BEAN_NAME;
import static callofproject.dev.service.interview.ServiceBeanName.TEST_PROPERTIES_FILE;
import static callofproject.dev.service.interview.Util.*;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EntityScan(basePackages = BeanName.REPOSITORY_PACKAGE)
@ComponentScan(basePackages = {SERVICE_BEAN_NAME, BeanName.REPOSITORY_PACKAGE})
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
public class ManagementServiceTests
{
    @Autowired
    private ICodingInterviewMapper m_codingInterviewMapper;

    @Autowired
    private ITestInterviewMapper m_testInterviewMapper;

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
    private TestInterview testInterview;

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
        var codingInterviewDTO = createCodingInterviewDTO(m_project.getProjectId(), List.of(participant1.getUserId().toString(), participant2.getUserId().toString(), participant3.getUserId().toString()));
        codingInterview = m_injection.getCodingInterviewRepository().save(m_codingInterviewMapper.toCodingInterview(codingInterviewDTO));
        m_project.setCodingInterview(codingInterview);
        m_project = m_injection.getProjectRepository().save(m_project);


        var interview = createTestInterviewDTO(m_project.getProjectId(), List.of(participant1.getUserId().toString(), participant2.getUserId().toString(), participant3.getUserId().toString()));
        testInterview = m_injection.getTestInterviewRepository().save(m_testInterviewMapper.toTestInterview(interview));
        var tiq = new TestInterviewQuestion("Test Question", 100, "Test Question", "A", "B", "C", "D", "A", testInterview, QuestionStatus.UNANSWERED);
        m_injection.getInterviewServiceHelper().saveQuestions(List.of(tiq));
        testInterview.setQuestions(Set.of(tiq));
        m_injection.getTestInterviewRepository().save(testInterview);
        m_project.setTestInterview(testInterview);
        m_project = m_injection.getProjectRepository().save(m_project);
    }

    @Test
    void testFindAllInterviewsByUserId_withGivenValidUserId_shouldReturnInterviews()
    {
        var result = m_injection.getManagementService().findAllInterviewsByUserId(projectOwner.getUserId());
        assertNotNull(result);
        assertEquals(2, result.getItemCount());
    }


    @Test
    void testFindAllInterviewsByUserId_withGivenInValidUserId_shouldReturnEmptyList()
    {
        var result = m_injection.getManagementService().findAllInterviewsByUserId(participant1.getUserId());
        assertNotNull(result);
        assertEquals(0, result.getItemCount());
    }

    @Test
    void testFindCodingInterviewOwner_withGivenValidProjectId_shouldReturnInterviews()
    {
        var result = m_injection.getManagementService().findCodingInterviewOwner(codingInterview.getCodingInterviewId());
        assertNotNull(result);
        assertEquals(Status.OK, result.getStatusCode());
        var list = (List<UserCodingInterviewDTOV2>) result.getObject();
        assertEquals(2, list.size());
    }

    @Test
    void testFindTestInterviewOwner_withGivenValidProjectId_shouldReturnInterviews()
    {
        var result = m_injection.getManagementService().findTestInterviewOwner(testInterview.getId());
        assertNotNull(result);
        assertEquals(Status.OK, result.getStatusCode());
        var list = (List<UserTestInterviewDTO>) result.getObject();
        assertEquals(2, list.size());
    }

    @AfterEach
    public void tearDown()
    {
        m_databaseCleaner.clearH2Database();
    }
}
