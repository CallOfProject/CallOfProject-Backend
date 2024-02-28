package callofproject.dev.service.interview.service.codinginterview;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.enums.NotificationType;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.service.interview.config.kafka.KafkaProducer;
import callofproject.dev.service.interview.data.dal.InterviewServiceHelper;
import callofproject.dev.service.interview.data.entity.CodingInterview;
import callofproject.dev.service.interview.data.entity.Project;
import callofproject.dev.service.interview.data.entity.ProjectParticipant;
import callofproject.dev.service.interview.data.entity.User;
import callofproject.dev.service.interview.dto.NotificationKafkaDTO;
import callofproject.dev.service.interview.dto.coding.CodingInterviewDTO;
import callofproject.dev.service.interview.dto.coding.CreateCodingInterviewDTO;
import callofproject.dev.service.interview.mapper.ICodingInterviewMapper;
import callofproject.dev.service.interview.mapper.IProjectMapper;
import callofproject.dev.service.interview.service.EInterviewStatus;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.StreamSupport;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

@Service
@Lazy
public class CodingInterviewCallbackService
{
    private final InterviewServiceHelper m_interviewServiceHelper;
    private final ICodingInterviewMapper m_codingInterviewMapper;
    private final IProjectMapper m_projectMapper;
    private final KafkaProducer m_kafkaProducer;

    public CodingInterviewCallbackService(InterviewServiceHelper interviewServiceHelper, ICodingInterviewMapper codingInterviewMapper, KafkaProducer kafkaProducer, IProjectMapper projectMapper)
    {
        m_interviewServiceHelper = interviewServiceHelper;
        m_codingInterviewMapper = codingInterviewMapper;
        m_kafkaProducer = kafkaProducer;
        m_projectMapper = projectMapper;
    }

    public ResponseMessage<Object> createCodeInterview(CreateCodingInterviewDTO dto)
    {
        var project = findProjectIfExistsById(dto.projectId());
        var codingInterviewCreateEntity = m_codingInterviewMapper.toCodingInterview(dto);
        // set project to coding interview and set coding interview to project
        codingInterviewCreateEntity.setProject(project);
        project.setCodingInterview(codingInterviewCreateEntity);
        // update project
        m_interviewServiceHelper.createProject(project);

        // set assigned users from project participants
        codingInterviewCreateEntity.setAssignedUsers(project.getProjectParticipants().stream().map(ProjectParticipant::getUser).collect(toSet()));
        // Create coding interview
        var savedInterview = doForDataService(() -> m_interviewServiceHelper.createCodeInterview(codingInterviewCreateEntity), "CodingInterviewCallbackService::createCodeInterview");
        var codingInterviewDTO = m_codingInterviewMapper.toCodingInterviewDTO(savedInterview, m_projectMapper.toProjectDTO(savedInterview.getProject()));

        return new ResponseMessage<>("Coding interview created successfully", Status.CREATED, codingInterviewDTO);
    }

    // After participants, remove interviews from project participants
    public ResponseMessage<Object> deleteCodeInterview(UUID ownerId, UUID codeInterviewId)
    {
        // find the owner
        var owner = findUserIfExistsById(ownerId);
        // find the interview
        var interview = findInterviewIfExistsById(codeInterviewId);

        // check if the owner is authorized to delete the interview
        if (!owner.getUserId().equals(interview.getProject().getProjectOwner().getUserId()))
            return new ResponseMessage<>("You are not authorized to delete this interview", Status.UNAUTHORIZED, null);

        var project = findProjectIfExistsById(interview.getProject().getProjectId());
        project.setCodingInterview(null);
        //project.setProjectParticipants(null);
        m_interviewServiceHelper.createProject(project);
        interview.getAssignedUsers().clear();
        m_interviewServiceHelper.deleteCodeInterview(interview);
        return new ResponseMessage<>("Coding interview deleted successfully", Status.OK, true);
    }

    public ResponseMessage<Object> deleteCodeInterviewByProjectId(UUID projectId)
    {
        var project = findProjectIfExistsById(projectId);
        return deleteCodeInterview(project.getProjectOwner().getUserId(), project.getCodingInterview().getCodingInterviewId());
    }


    public ResponseMessage<Object> addParticipant(UUID codeInterviewId, UUID userId)
    {
        var interview = findInterviewIfExistsById(codeInterviewId);
        var user = findUserIfExistsById(userId);

        interview.addAssignedUser(user);
        user.addCodingInterview(interview);

        m_interviewServiceHelper.createCodeInterview(interview);
        m_interviewServiceHelper.saveUser(user);

        var dto = m_codingInterviewMapper.toCodingInterviewDTO(interview, m_projectMapper.toProjectDTO(interview.getProject()));
        return new ResponseMessage<>("Participant added successfully", Status.OK, dto);
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

        m_interviewServiceHelper.saveUser(user);
        m_interviewServiceHelper.createCodeInterview(interview);

        var p = m_projectMapper.toProjectDTO(interview.getProject());
        var dto = m_codingInterviewMapper.toCodingInterviewDTO(interview, p);


        return new ResponseMessage<>("Participant removed successfully", Status.OK, dto);
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


    private void send(UUID owner, UUID userId, String message)
    {
        var notificationDTO = new NotificationKafkaDTO.Builder()
                .setFromUserId(owner)
                .setToUserId(userId)
                .setMessage(message)
                .setNotificationType(NotificationType.INFORMATION)
                //.setNotificationData(project)
                .build();
        m_kafkaProducer.sendNotification(notificationDTO);
    }

    public void sendNotification(CodingInterviewDTO object, EInterviewStatus status)
    {
        var project = findProjectIfExistsById(object.projectDTO().projectId());
        var participants = project.getCodingInterview().getAssignedUsers();
        var message = "A coding interview has been %s for the Size %s Project application";

        switch (status)
        {
            case CREATED ->
                    participants.forEach(p -> send(project.getProjectOwner().getUserId(), p.getUserId(), format(message, "created", project.getProjectName())));
            case REMOVED ->
                    participants.forEach(p -> send(project.getProjectOwner().getUserId(), p.getUserId(), format(message, "removed", project.getProjectName())));
            case ASSIGNED ->
                    participants.forEach(p -> send(project.getProjectOwner().getUserId(), p.getUserId(), format(message, "assigned", project.getProjectName())));
            default -> throw new DataServiceException("Invalid status");
        }
    }

    public void sendNotification(boolean result, UUID interviewId, EInterviewStatus status)
    {
        // send notification to Owner and Participants
    }

    public void sendNotification(UUID projectId, boolean result, EInterviewStatus status)
    {
        var project = findProjectIfExistsById(projectId);
        var participants = project.getCodingInterview().getAssignedUsers();
        var owner = project.getCodingInterview().getProject().getProjectOwner().getUserId();
        participants.forEach(p -> {
            send(owner, p.getUserId(), "");
        });

    }
}
