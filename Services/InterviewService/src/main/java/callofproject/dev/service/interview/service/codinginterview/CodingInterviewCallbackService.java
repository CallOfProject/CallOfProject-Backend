package callofproject.dev.service.interview.service.codinginterview;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.service.interview.data.dal.InterviewServiceHelper;
import callofproject.dev.service.interview.data.entity.CodingInterview;
import callofproject.dev.service.interview.data.entity.Project;
import callofproject.dev.service.interview.data.entity.User;
import callofproject.dev.service.interview.dto.coding.CreateCodingInterviewDTO;
import callofproject.dev.service.interview.mapper.ICodingInterviewMapper;
import callofproject.dev.service.interview.mapper.IProjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.StreamSupport;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;

@Service
@Lazy
public class CodingInterviewCallbackService
{
    private final InterviewServiceHelper m_interviewServiceHelper;
    private final ICodingInterviewMapper m_codingInterviewMapper;
    private final IProjectMapper m_projectMapper;

    public CodingInterviewCallbackService(InterviewServiceHelper interviewServiceHelper, ICodingInterviewMapper codingInterviewMapper, IProjectMapper projectMapper)
    {
        m_interviewServiceHelper = interviewServiceHelper;
        m_codingInterviewMapper = codingInterviewMapper;
        m_projectMapper = projectMapper;
    }

    public ResponseMessage<Object> createCodeInterview(CreateCodingInterviewDTO dto)
    {
        // to dto
        var codingInterviewCreateEntity = m_codingInterviewMapper.toCodingInterview(dto);

        var project = findProjectIfExistsById(dto.projectId());
        codingInterviewCreateEntity.setProject(project);
        project.setCodingInterview(codingInterviewCreateEntity);
        m_interviewServiceHelper.createProject(project);
        // create coding interview
        var result = doForDataService(() -> m_interviewServiceHelper.createCodeInterview(codingInterviewCreateEntity),
                "CodingInterviewCallbackService.createCodeInterview: Error creating coding interview");

        var p = m_projectMapper.toProjectDTO(result.getProject());
        var codingDto = m_codingInterviewMapper.toCodingInterviewDTO(result, p);

        return new ResponseMessage<>("Coding interview created successfully", Status.CREATED, codingDto);
    }

    public ResponseMessage<Object> deleteCodeInterview(UUID ownerId, UUID codeInterviewId)
    {
        var owner = findUserIfExistsById(ownerId);
        var interview = findInterviewIfExistsById(codeInterviewId);

        if (!owner.getUserId().equals(interview.getProject().getProjectOwner().getUserId()))
            return new ResponseMessage<>("You are not authorized to delete this interview", Status.UNAUTHORIZED, null);

        m_interviewServiceHelper.deleteCodeInterview(interview);

        return new ResponseMessage<>("Coding interview deleted successfully", Status.OK, null);
    }

    public ResponseMessage<Object> deleteCodeInterviewByProjectId(UUID projectId)
    {
        var project = findProjectIfExistsById(projectId);

        m_interviewServiceHelper.deleteCodeInterview(project.getCodingInterview());

        return new ResponseMessage<>("Coding interview deleted successfully", Status.OK, project);
    }


    public ResponseMessage<Object> addParticipant(UUID codeInterviewId, UUID userId)
    {
        var interview = findInterviewIfExistsById(codeInterviewId);
        var user = findUserIfExistsById(userId);

        interview.addAssignedUser(user);
        user.addCodingInterview(interview);

        m_interviewServiceHelper.createCodeInterview(interview);

        return new ResponseMessage<>("Participant added successfully", Status.OK, interview);
    }

    public ResponseMessage<Object> addParticipantByProjectId(UUID projectId, UUID userId)
    {
        var user = findUserIfExistsById(userId);
        var project = findProjectIfExistsById(projectId);

        return addParticipant(project.getCodingInterview().getCodingInterviewId(), user.getUserId());
    }

    public ResponseMessage<Object> removeParticipant(UUID codeInterviewId, UUID userId)
    {
        var interview = findInterviewIfExistsById(codeInterviewId);
        var user = findUserIfExistsById(userId);

        interview.getAssignedUsers().removeIf(u -> u.getUserId().equals(userId));
        user.getCodingInterviews().removeIf(i -> i.getCodingInterviewId().equals(codeInterviewId));

        m_interviewServiceHelper.createCodeInterview(interview);


        return new ResponseMessage<>("Participant removed successfully", Status.OK, interview);
    }

    public ResponseMessage<Object> removeParticipantByProjectId(UUID projectId, UUID userId)
    {
        var user = findUserIfExistsById(userId);
        var project = findProjectIfExistsById(projectId);

        return removeParticipant(project.getCodingInterview().getCodingInterviewId(), user.getUserId());
    }

    public MultipleResponseMessage<Object> getParticipantsByProjectId(UUID projectId)
    {
        var project = findProjectIfExistsById(projectId);
        var users = project.getCodingInterview().getAssignedUsers().stream().toList();
        return new MultipleResponseMessage<>(users.size(), "Participants found successfully", users);
    }

    public MultipleResponseMessage<Object> getParticipants(UUID codeInterviewId)
    {
        var interview = findInterviewIfExistsById(codeInterviewId);
        var users = interview.getAssignedUsers().stream().toList();
        return new MultipleResponseMessage<>(users.size(), "Participants found successfully", users);
    }

    public ResponseMessage<Object> getInterviewByProjectId(UUID projectId)
    {
        var project = findProjectIfExistsById(projectId);
        return new ResponseMessage<>("Interview found successfully", Status.OK, project.getCodingInterview());
    }

    public ResponseMessage<Object> getInterview(UUID codeInterviewId)
    {
        var interview = findInterviewIfExistsById(codeInterviewId);
        return new ResponseMessage<>("Interview found successfully", Status.OK, interview);
    }

    public MultipleResponseMessage<Object> getAllInterviews()
    {
        var interviews = StreamSupport.stream(m_interviewServiceHelper.findAllInterviews().spliterator(), false).toList();
        return new MultipleResponseMessage<>(interviews.size(), "Interviews found successfully", interviews);
    }


    // helper methods

    private CodingInterview findInterviewIfExistsById(UUID codeInterviewId)
    {
        var interview = m_interviewServiceHelper.findCodingInterviewById(codeInterviewId);

        if (interview.isEmpty())
            throw new DataServiceException("Interview not found");

        return interview.get();
    }


    private User findUserIfExistsById(UUID userId)
    {
        var user = m_interviewServiceHelper.findUserById(userId);

        if (user.isEmpty())
            throw new DataServiceException("User not found");

        return user.get();
    }


    private Project findProjectIfExistsById(UUID projectId)
    {
        var project = m_interviewServiceHelper.findProjectById(projectId);

        if (project.isEmpty())
            throw new DataServiceException("Project not found");

        return project.get();
    }
}
