package callofproject.dev.data.interview.dal;

import callofproject.dev.data.interview.entity.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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

    public Project createProject(Project project)
    {
        return doForRepository(() -> m_repositoryFacade.m_projectRepository.save(project), "Error creating project");
    }

    public ProjectParticipant createProjectParticipant(ProjectParticipant participant)
    {
        return doForRepository(() -> m_repositoryFacade.m_projectParticipantRepository.save(participant), "Error creating project participant");
    }

    public UserCodingInterviews createUserCodingInterviews(UserCodingInterviews userCodingInterviews)
    {
        return doForRepository(() -> m_repositoryFacade.m_userCodingInterviewsRepository.save(userCodingInterviews), "Error creating user coding interviews");
    }

    public UserTestInterviews createUserTestInterviews(UserTestInterviews userTestInterviews)
    {
        return doForRepository(() -> m_repositoryFacade.m_userTestInterviewsRepository.save(userTestInterviews), "Error creating user test interviews");
    }

    public Iterable<UserCodingInterviews> findCodingInterviewParticipantsById(UUID codingInterviewId)
    {
        return doForRepository(() -> m_repositoryFacade.m_userCodingInterviewsRepository.findParticipantsByCodingInterview_Id(codingInterviewId), "Error finding coding interview participants by id");
    }

    public void removeCodingInterviewParticipants(List<UUID> list)
    {
        doForRepository(() -> m_repositoryFacade.m_userCodingInterviewsRepository.deleteAllById(list), "Error removing coding interview participants");
    }

    public UserCodingInterviews findUserCodingInterviewByUserIdAndInterviewId(UUID userId, UUID codeInterviewId)
    {
        return doForRepository(() -> m_repositoryFacade.m_userCodingInterviewsRepository.findUserCodingInterviewsByUserIdAndCodingInterviewId(userId, codeInterviewId), "Error finding user coding interview by user id and interview id");
    }

    public void removeUserCodingInterview(UserCodingInterviews userCodingInterview)
    {
        doForRepository(() -> m_repositoryFacade.m_userCodingInterviewsRepository.delete(userCodingInterview), "Error removing user coding interview");
    }

    public Iterable<Project> findOwnerProjectsByUserId(UUID userId)
    {
        return doForRepository(() -> m_repositoryFacade.m_projectRepository.findOwnerProjectsByUserId(userId), "Error finding projects by user id");
    }

    public Optional<UserTestInterviews> findUserTestInterviewByUserAndTestInterviewId(UUID userId, UUID id)
    {
        return doForRepository(() -> m_repositoryFacade.m_userTestInterviewsRepository.findUserTestInterviewsByUserAndTestInterviewId(userId, id), "Error finding user test interview by user and test interview id");
    }

    public QuestionAnswer createQuestionAnswer(QuestionAnswer questionAnswer)
    {
        return doForRepository(() -> m_repositoryFacade.m_questionAnswerRepository.save(questionAnswer), "Error creating question answer");
    }


    public Iterable<TestInterview> findAllTestInterviews()
    {
        return doForRepository(m_repositoryFacade.m_testInterviewRepository::findAll, "Error finding all test interviews");
    }


    public Iterable<CodingInterview> findAllInterviewsByEnDate(LocalDateTime endDate)
    {
        return doForRepository(() -> m_repositoryFacade.m_codingInterviewRepository.findAllInterviewsByEnDate(endDate), "Error finding all coding interviews");
    }

    public Iterable<TestInterview> findAllTestInterviewsByEnDate(LocalDateTime endDate)
    {
        return doForRepository(() -> m_repositoryFacade.m_testInterviewRepository.findAllTestInterviewsByEnDate(endDate), "Error finding all coding interviews");
    }
}
