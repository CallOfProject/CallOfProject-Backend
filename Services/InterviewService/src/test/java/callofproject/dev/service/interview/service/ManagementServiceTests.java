package callofproject.dev.service.interview.service;

import callofproject.dev.data.interview.BeanName;
import callofproject.dev.data.interview.entity.*;
import callofproject.dev.data.interview.entity.enums.AdminOperationStatus;
import callofproject.dev.data.interview.entity.enums.EProjectStatus;
import callofproject.dev.service.interview.DatabaseCleaner;
import callofproject.dev.service.interview.Injection;
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

    @BeforeEach
    public void setUp()
    {
        projectOwner = m_injection.getUserRepository().save(createUser("owner"));
        participant1 = m_injection.getUserRepository().save(createUser("participant1"));
        var participant2 = m_injection.getUserRepository().save(createUser("participant2"));
        var participant3 = m_injection.getUserRepository().save(createUser("participant3"));

        var project = new Project(randomUUID(), "Test Project -" + ms_random.nextInt(100), projectOwner);

        project.addProjectParticipant(new ProjectParticipant(randomUUID(), project, participant1, LocalDateTime.now()));
        project.addProjectParticipant(new ProjectParticipant(randomUUID(), project, participant2, LocalDateTime.now()));
        project.addProjectParticipant(new ProjectParticipant(randomUUID(), project, participant3, LocalDateTime.now()));

        project.setProjectStatus(EProjectStatus.IN_PROGRESS);
        project.setAdminOperationStatus(AdminOperationStatus.ACTIVE);

        project = m_injection.getInterviewServiceHelper().createProject(project);
        var codingInterviewDTO = createCodingInterviewDTO(project.getProjectId(), List.of(participant1.getUserId().toString(), participant2.getUserId().toString(), participant3.getUserId().toString()));
        var codingInterview = m_injection.getCodingInterviewRepository().save(m_codingInterviewMapper.toCodingInterview(codingInterviewDTO));
        project.setCodingInterview(codingInterview);
        project = m_injection.getProjectRepository().save(project);


        var interview = createTestInterviewDTO(project.getProjectId(), List.of(participant1.getUserId().toString(), participant2.getUserId().toString(), participant3.getUserId().toString()));
        var testInterview = m_injection.getTestInterviewRepository().save(m_testInterviewMapper.toTestInterview(interview));
        var tiq = new TestInterviewQuestion("Test Question", 100, "Test Question", "A", "B", "C", "D", "A", testInterview, QuestionStatus.UNANSWERED);
        m_injection.getInterviewServiceHelper().saveQuestions(List.of(tiq));
        testInterview.setQuestions(Set.of(tiq));
        m_injection.getTestInterviewRepository().save(testInterview);
        project.setTestInterview(testInterview);
        project = m_injection.getProjectRepository().save(project);
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


    @AfterEach
    public void tearDown()
    {
        m_databaseCleaner.clearH2Database();
    }
}