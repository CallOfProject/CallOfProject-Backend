package callofproject.dev.service.interview.data.dal;

import callofproject.dev.service.interview.data.entity.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForRepository;

@Component
@Lazy
public class InterviewServiceHelper
{
    private final RepositoryFacade m_repositoryFacade;

    public InterviewServiceHelper(RepositoryFacade repositoryFacade)
    {
        m_repositoryFacade = repositoryFacade;
    }


    public CodingInterview createCodeInterview(CodingInterview codingInterview)
    {
        return doForRepository(() -> m_repositoryFacade.m_codingInterviewRepository.save(codingInterview), "Error creating coding interview");
    }

    public void deleteCodeInterview(CodingInterview codingInterview)
    {
        doForRepository(() -> m_repositoryFacade.m_codingInterviewRepository.delete(codingInterview), "Error deleting coding interview");
    }


    public Optional<CodingInterview> findCodingInterviewById(UUID codeInterviewId)
    {
        return doForRepository(() -> m_repositoryFacade.m_codingInterviewRepository.findById(codeInterviewId), "Error finding coding interview by id");
    }

    public Optional<User> findUserById(UUID userId)
    {
        return doForRepository(() -> m_repositoryFacade.m_userRepository.findById(userId), "Error finding user by id");
    }

    public Optional<Project> findProjectById(UUID projectId)
    {
        return doForRepository(() -> m_repositoryFacade.m_projectRepository.findById(projectId), "Error finding project by id");
    }

    public Iterable<CodingInterview> findAllInterviews()
    {
        return doForRepository(m_repositoryFacade.m_codingInterviewRepository::findAll, "Error finding all coding interviews");
    }

    public TestInterview createInterview(TestInterview testInterview)
    {
        return doForRepository(() -> m_repositoryFacade.m_testInterviewRepository.save(testInterview), "Error creating test interview");
    }

    public Iterable<TestInterviewQuestion> saveQuestions(List<TestInterviewQuestion> questions)
    {
        return doForRepository(() -> m_repositoryFacade.m_testInterviewQuestionRepository.saveAll(questions), "Error saving test interview questions");
    }

    public Optional<TestInterview> findTestInterviewById(UUID testInterviewId)
    {
        return doForRepository(() -> m_repositoryFacade.m_testInterviewRepository.findById(testInterviewId), "Error finding test interview by id");
    }

    public TestInterviewQuestion saveQuestion(TestInterviewQuestion question)
    {
        return doForRepository(() -> m_repositoryFacade.m_testInterviewQuestionRepository.save(question), "Error saving test interview question");
    }

    public void deleteTestInterview(TestInterview interview)
    {
        doForRepository(() -> m_repositoryFacade.m_testInterviewRepository.delete(interview), "Error deleting test interview");
    }

    public User saveUser(User user)
    {
        return doForRepository(() -> m_repositoryFacade.m_userRepository.save(user), "Error saving user");
    }

    public Iterable<User> findUsersByIds(List<UUID> uuids)
    {
        return doForRepository(() -> m_repositoryFacade.m_userRepository.findAllById(uuids), "Error finding users by ids");
    }

    public Iterable<User> saveUsers(Set<User> users)
    {
        return doForRepository(() -> m_repositoryFacade.m_userRepository.saveAll(users), "Error saving users");
    }

    public Optional<TestInterviewQuestion> findQuestionById(long questionId)
    {
        return doForRepository(() -> m_repositoryFacade.m_testInterviewQuestionRepository.findById(questionId), "Error finding question by id");
    }

    public void deleteQuestion(TestInterviewQuestion question)
    {
        doForRepository(() -> m_repositoryFacade.m_testInterviewQuestionRepository.delete(question), "Error deleting question");
    }
}
