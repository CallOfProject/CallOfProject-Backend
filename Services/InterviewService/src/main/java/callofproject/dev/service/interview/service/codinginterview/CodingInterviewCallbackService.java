package callofproject.dev.service.interview.service.codinginterview;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.data.common.enums.EmailType;
import callofproject.dev.data.common.enums.NotificationType;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.service.interview.config.kafka.KafkaProducer;
import callofproject.dev.data.interview.dal.InterviewServiceHelper;
import callofproject.dev.data.interview.entity.*;
import callofproject.dev.data.interview.entity.enums.InterviewStatus;
import callofproject.dev.service.interview.dto.NotificationKafkaDTO;
import callofproject.dev.service.interview.dto.OwnerProjectDTO;
import callofproject.dev.service.interview.dto.coding.CodingInterviewDTO;
import callofproject.dev.service.interview.dto.coding.CreateCodingInterviewDTO;
import callofproject.dev.service.interview.mapper.ICodingInterviewMapper;
import callofproject.dev.service.interview.mapper.IProjectMapper;
import callofproject.dev.service.interview.mapper.IUserMapper;
import callofproject.dev.service.interview.service.EInterviewStatus;
import callofproject.dev.service.interview.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.StreamSupport.stream;

@Service
@Lazy
public class CodingInterviewCallbackService
{
    private final InterviewServiceHelper m_interviewServiceHelper;
    private final ICodingInterviewMapper m_codingInterviewMapper;
    private final S3Service m_s3Service;
    private final IProjectMapper m_projectMapper;
    private final IUserMapper m_userMapper;
    private final KafkaProducer m_kafkaProducer;
    @Value("${coding-interview.email.template}")
    private String m_interviewEmail;
    public CodingInterviewCallbackService(InterviewServiceHelper interviewServiceHelper, ICodingInterviewMapper codingInterviewMapper, S3Service s3Service, KafkaProducer kafkaProducer, IProjectMapper projectMapper, IUserMapper userMapper)
    {
        m_interviewServiceHelper = interviewServiceHelper;
        m_codingInterviewMapper = codingInterviewMapper;
        m_s3Service = s3Service;
        m_kafkaProducer = kafkaProducer;
        m_projectMapper = projectMapper;
        m_userMapper = userMapper;
    }

    public ResponseMessage<Object> createCodeInterview(CreateCodingInterviewDTO dto)
    {
        var project = findProjectIfExistsById(dto.projectId());

        if (project.getCodingInterview() != null)
            deleteCodeInterview(project.getProjectOwner().getUserId(), project.getCodingInterview().getCodingInterviewId());

        var codingInterviewCreateEntity = m_codingInterviewMapper.toCodingInterview(dto);
        // set project to coding interview and set coding interview to project
        codingInterviewCreateEntity.setProject(project);
        project.setCodingInterview(codingInterviewCreateEntity);
        // update project
        m_interviewServiceHelper.createProject(project);

        // Create coding interview
        var savedInterview = doForDataService(() -> m_interviewServiceHelper.createCodeInterview(codingInterviewCreateEntity),
                "CodingInterviewCallbackService::createCodeInterview");
        var users = dto.userIds().stream().map(UUID::fromString).map(this::findUserIfExistsById).collect(toSet());

        // Create user coding interviews
        users.stream().map(u -> new UserCodingInterviews(u, savedInterview)).forEach(m_interviewServiceHelper::createUserCodingInterviews);

        var codingInterviewDTO = m_codingInterviewMapper.toCodingInterviewDTO(savedInterview, m_projectMapper.toProjectDTO(savedInterview.getProject()));
        sendEmails(codingInterviewDTO, users.stream().toList());
        return new ResponseMessage<>("Coding interview created successfully", Status.CREATED, codingInterviewDTO);
    }

    // After participants, remove interviews from project participants
    public ResponseMessage<Object> deleteCodeInterview(UUID ownerId, UUID codeInterviewId)
    {
        // find the owner
        var owner = findUserIfExistsById(ownerId);
        // find the interview
        var interview = findInterviewIfExistsById(codeInterviewId);
        var interviewParticipants = stream(m_interviewServiceHelper.findCodingInterviewParticipantsById(interview.getCodingInterviewId()).spliterator(), false);

        // check if the owner is authorized to delete the interview
        if (!owner.getUserId().equals(interview.getProject().getProjectOwner().getUserId()))
            return new ResponseMessage<>("You are not authorized to delete this interview", Status.UNAUTHORIZED, null);

        var project = findProjectIfExistsById(interview.getProject().getProjectId());
        project.setCodingInterview(null);
        // Update project
        m_interviewServiceHelper.createProject(project);

        // Remove Interview participants
        interview.getCodingInterviews().stream().map(UserCodingInterviews::getUser).forEach(u -> u.getCodingInterviews().removeIf(ci -> ci.getCodingInterview().getCodingInterviewId().equals(interview.getCodingInterviewId())));
        interview.getCodingInterviews().clear();
        m_interviewServiceHelper.removeCodingInterviewParticipants(interviewParticipants.map(UserCodingInterviews::getId).toList());
        // Delete interview
        m_interviewServiceHelper.deleteCodeInterview(interview);
        var dto = m_codingInterviewMapper.toCodingInterviewDTO(interview, m_projectMapper.toProjectDTO(project));
        return new ResponseMessage<>("Coding interview deleted successfully", Status.OK, dto);
    }

    private void sendEmails(CodingInterviewDTO codingInterviewDTO, List<User> list)
    {
        list.forEach(u -> sendEmail(codingInterviewDTO.codingInterviewId(), u.getEmail(), codingInterviewDTO.projectDTO().projectName(), u.getUserId()));
    }

    private void sendEmail(UUID interviewId, String email, String projectName, UUID userId)
    {
        System.out.println("EMAIL: " + email);
        var title = "Test Interview Assigned for " + projectName;
        var emailStr = String.format(m_interviewEmail, interviewId, userId);
        var topic = new EmailTopic(EmailType.ASSIGN_INTERVIEW, email, title, emailStr, null);
        m_kafkaProducer.sendEmail(topic);
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

        // Create user coding interview
        var userCodingInterview = new UserCodingInterviews(user, interview);
        user.addCodingInterview(userCodingInterview);

        // Save user so update user coding interview
        m_interviewServiceHelper.saveUser(user);

        // convert to CodingInterviewDTO class
        var dto = m_codingInterviewMapper.toCodingInterviewDTO(interview, m_projectMapper.toProjectDTO(interview.getProject()));
        return new ResponseMessage<>("Participant added successfully", Status.OK, dto);
    }

    public ResponseMessage<Object> addParticipantByProjectId(UUID projectId, UUID userId)
    {
        var project = findProjectIfExistsById(projectId);

        return addParticipant(project.getCodingInterview().getCodingInterviewId(), userId);
    }

    public ResponseMessage<Object> removeParticipant(UUID codeInterviewId, UUID userId)
    {
        var interview = findInterviewIfExistsById(codeInterviewId);
        var user = findUserIfExistsById(userId);

        // Find user coding interview
        var userCodingInterview = m_interviewServiceHelper.findUserCodingInterviewByUserIdAndInterviewId(user.getUserId(), codeInterviewId);

        // Remove user coding interview from user and codingInterview then delete it from the database
        user.getCodingInterviews().removeIf(ci -> ci.getId().equals(userCodingInterview.getId()));
        interview.getCodingInterviews().removeIf(ci -> ci.getId().equals(userCodingInterview.getId()));
        m_interviewServiceHelper.removeUserCodingInterview(userCodingInterview);

        // Convert to CodingInterviewDTO class
        var dto = m_codingInterviewMapper.toCodingInterviewDTO(interview, m_projectMapper.toProjectDTO(interview.getProject()));

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
        return getParticipants(project.getCodingInterview().getCodingInterviewId());
    }

    public MultipleResponseMessage<Object> getParticipants(UUID codeInterviewId)
    {
        findInterviewIfExistsById(codeInterviewId); // if not found, it will throw an exception
        var userCodingInterview = stream(m_interviewServiceHelper.findCodingInterviewParticipantsById(codeInterviewId).spliterator(), false);
        var users = userCodingInterview.map(UserCodingInterviews::getUser).map(m_userMapper::toUserDTO).toList();
        return new MultipleResponseMessage<>(users.size(), "Participants found successfully", users);
    }

    public ResponseMessage<Object> getInterviewByProjectId(UUID projectId)
    {
        var project = findProjectIfExistsById(projectId);
        var projectDTO = m_projectMapper.toProjectDTO(project);
        var codingInterviewDTO = m_codingInterviewMapper.toCodingInterviewDTO(project.getCodingInterview(), projectDTO);
        return new ResponseMessage<>("Interview found successfully", Status.OK, codingInterviewDTO);
    }

    public ResponseMessage<Object> getInterview(UUID codeInterviewId)
    {
        var interview = findInterviewIfExistsById(codeInterviewId);
        var projectDTO = m_projectMapper.toProjectDTO(interview.getProject());
        var codingInterviewDTO = m_codingInterviewMapper.toCodingInterviewDTO(interview, projectDTO);
        return new ResponseMessage<>("Interview found successfully", Status.OK, codingInterviewDTO);
    }

    public ResponseMessage<Object> submitInterview(UUID userId, UUID codeInterviewId, MultipartFile file)
    {
        var userCodingInterview = m_interviewServiceHelper.findUserCodingInterviewByUserIdAndInterviewId(userId, codeInterviewId);
        userCodingInterview.setAnswerFileName(Objects.requireNonNull(file.getOriginalFilename()));
        userCodingInterview.setInterviewStatus(InterviewStatus.COMPLETED);
        // upload file to s3
        var result = m_s3Service.uploadToS3WithMultiPartFile(file, Objects.requireNonNull(file.getOriginalFilename()));
        userCodingInterview.setAnswerFileUrl(result);
        m_interviewServiceHelper.createUserCodingInterviews(userCodingInterview);

        return new ResponseMessage<>("Interview submitted successfully", Status.OK, result);
    }


    public MultipleResponseMessage<Object> findUserInterviewInformation(UUID userId)
    {
        findUserIfExistsById(userId);
        var projects = stream(m_interviewServiceHelper.findOwnerProjectsByUserId(userId).spliterator(), false);
        var projectsOwnerDTO = m_projectMapper.toOwnerProjectsDTO(projects.map(this::toOwnerProjectDTO).toList());
        return new MultipleResponseMessage<>(projectsOwnerDTO.ownerProjects().size(), "Projects are found!", projectsOwnerDTO);
    }

    private OwnerProjectDTO toOwnerProjectDTO(Project p)
    {
        var participantsDTO = m_projectMapper.toProjectsParticipantDTO(p.getProjectParticipants().stream().map(m_projectMapper::toProjectParticipantDTO).toList());
        return m_projectMapper.toOwnerProjectDTO(p, participantsDTO);
    }


    public ResponseMessage<Object> isUserSolvedBefore(UUID userId, UUID interviewId)
    {
        var userCodingInterview = m_interviewServiceHelper.findUserCodingInterviewByUserIdAndInterviewId(userId, interviewId);

        if (userCodingInterview == null)
            return new ResponseMessage<>("User not found", Status.NOT_FOUND, false);

        var result = userCodingInterview.getInterviewStatus() == InterviewStatus.COMPLETED;

        return new ResponseMessage<>("User solved before", Status.OK, result);
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
        var notificationMessage = new NotificationKafkaDTO.Builder()
                .setFromUserId(owner)
                .setToUserId(userId)
                .setMessage(message)
                .setNotificationType(NotificationType.INFORMATION)
                .setNotificationLink("none")
                .setMessage(message)
                .setNotificationImage(null)
                .setNotificationTitle("Interview Status")
                .build();

        m_kafkaProducer.sendNotification(notificationMessage);
    }

    public void sendNotification(CodingInterviewDTO object, EInterviewStatus status)
    {
        //var interview = findInterviewIfExistsById(object.codingInterviewId());
        var project = findProjectIfExistsById(object.projectDTO().projectId());


        var participants = project.getProjectParticipants().stream().map(ProjectParticipant::getUser).toList();
        var message = "A coding interview has been %s for the Size %s Project application";

        switch (status)
        {
            case CREATED ->
                    participants.forEach(p -> send(project.getProjectOwner().getUserId(), p.getUserId(), format(message, "created", project.getProjectName())));
            case REMOVED ->
                    participants.forEach(p -> send(project.getProjectOwner().getUserId(), p.getUserId(), format(message, "removed", project.getProjectName())));
            case ASSIGNED ->
                    participants.forEach(p -> send(project.getProjectOwner().getUserId(), p.getUserId(), format(message, "assigned", project.getProjectName())));
            case CANCELLED ->
                    participants.forEach(p -> send(project.getProjectOwner().getUserId(), p.getUserId(), format(message, "cancelled", project.getProjectName())));
            default -> throw new DataServiceException("Invalid status");
        }
    }
}
