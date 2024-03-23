package callofproject.dev.service.interview.service.codinginterview;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.dsa.Pair;
import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.data.common.enums.EmailType;
import callofproject.dev.data.common.enums.NotificationType;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.interview.dal.InterviewServiceHelper;
import callofproject.dev.data.interview.entity.*;
import callofproject.dev.data.interview.entity.enums.InterviewResult;
import callofproject.dev.data.interview.entity.enums.InterviewStatus;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.service.interview.config.kafka.KafkaProducer;
import callofproject.dev.service.interview.dto.InterviewResultDTO;
import callofproject.dev.service.interview.dto.NotificationKafkaDTO;
import callofproject.dev.service.interview.dto.OwnerProjectDTO;
import callofproject.dev.service.interview.dto.UserEmailDTO;
import callofproject.dev.service.interview.dto.coding.CodingInterviewDTO;
import callofproject.dev.service.interview.dto.coding.CreateCodingInterviewDTO;
import callofproject.dev.service.interview.mapper.ICodingInterviewMapper;
import callofproject.dev.service.interview.mapper.IProjectMapper;
import callofproject.dev.service.interview.mapper.IUserMapper;
import callofproject.dev.service.interview.service.EInterviewStatus;
import callofproject.dev.service.interview.service.S3Service;
import callofproject.dev.service.interview.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.StreamSupport.stream;

/**
 * @author Nuri Can ÖZTÜRK
 * Represents the class coding interview service business logic.
 * CopyRight(C) 2023 by Call Of Project Teams.
 */
@Service
@Lazy
@SuppressWarnings("unused")
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

    /**
     * Constructor.
     *
     * @param interviewServiceHelper represents the interview service helper
     * @param codingInterviewMapper  represents the coding interview mapper
     * @param s3Service              represents the s3 service
     * @param kafkaProducer          represents the kafka producer
     * @param projectMapper          represents the project mapper
     * @param userMapper             represents the user mapper
     */
    public CodingInterviewCallbackService(InterviewServiceHelper interviewServiceHelper, ICodingInterviewMapper codingInterviewMapper, S3Service s3Service, KafkaProducer kafkaProducer, IProjectMapper projectMapper, IUserMapper userMapper)
    {
        m_interviewServiceHelper = interviewServiceHelper;
        m_codingInterviewMapper = codingInterviewMapper;
        m_s3Service = s3Service;
        m_kafkaProducer = kafkaProducer;
        m_projectMapper = projectMapper;
        m_userMapper = userMapper;
    }

    /**
     * Create code interview
     *
     * @param dto the dto
     * @return the response message
     */
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
        var userList = users.stream().map(m_userMapper::toUserEmailDTO).toList();
        return new ResponseMessage<>("Coding interview created successfully", Status.CREATED, new Pair<>(codingInterviewDTO, userList));
    }

    /**
     * Delete code interview
     *
     * @param ownerId         the owner id
     * @param codeInterviewId the code interview id
     * @return the response message
     */
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


    /**
     * Delete code interview by project id
     *
     * @param projectId the project id
     * @return the response message
     */
    public ResponseMessage<Object> deleteCodeInterviewByProjectId(UUID projectId)
    {
        var project = findProjectIfExistsById(projectId);
        return deleteCodeInterview(project.getProjectOwner().getUserId(), project.getCodingInterview().getCodingInterviewId());
    }


    /**
     * Add participant
     *
     * @param codeInterviewId the code interview id
     * @param userId          the user id
     * @return the response message
     */
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

    /**
     * Add participant by project id
     *
     * @param projectId the project id
     * @param userId    the user id
     * @return the response message
     */
    public ResponseMessage<Object> addParticipantByProjectId(UUID projectId, UUID userId)
    {
        var project = findProjectIfExistsById(projectId);

        return addParticipant(project.getCodingInterview().getCodingInterviewId(), userId);
    }

    /**
     * Remove participant
     *
     * @param codeInterviewId the code interview id
     * @param userId          the user id
     * @return the response message
     */
    public ResponseMessage<Object> removeParticipant(UUID codeInterviewId, UUID userId)
    {
        var interview = findInterviewIfExistsById(codeInterviewId);
        var user = findUserIfExistsById(userId);

        // Find user coding interview
        var userCodingInterview = m_interviewServiceHelper.findUserCodingInterviewByUserIdAndInterviewId(user.getUserId(), codeInterviewId);

        // Remove user coding interview from user and codingInterview then delete it from the database
        user.getCodingInterviews().removeIf(ci -> ci.getId().equals(userCodingInterview.getId()));
        interview.getCodingInterviews().removeIf(ci -> ci.getId().equals(userCodingInterview.getId()));
        userCodingInterview.setCodingInterview(null);
        m_interviewServiceHelper.removeUserCodingInterview(userCodingInterview);

        // Convert to CodingInterviewDTO class
        var dto = m_codingInterviewMapper.toCodingInterviewDTO(interview, m_projectMapper.toProjectDTO(interview.getProject()));

        return new ResponseMessage<>("Participant removed successfully", Status.OK, dto);
    }

    /**
     * Remove participant by project id
     *
     * @param projectId the project id
     * @param userId    the user id
     * @return the response message
     */
    public ResponseMessage<Object> removeParticipantByProjectId(UUID projectId, UUID userId)
    {
        var user = findUserIfExistsById(userId);
        var project = findProjectIfExistsById(projectId);

        return removeParticipant(project.getCodingInterview().getCodingInterviewId(), user.getUserId());
    }

    /**
     * Get participants by project id
     *
     * @param projectId the project id
     * @return the response message
     */
    public MultipleResponseMessage<Object> getParticipantsByProjectId(UUID projectId)
    {
        var project = findProjectIfExistsById(projectId);
        return getParticipants(project.getCodingInterview().getCodingInterviewId());
    }

    /**
     * Get participants
     *
     * @param codeInterviewId the code interview id
     * @return the response message
     */
    public MultipleResponseMessage<Object> getParticipants(UUID codeInterviewId)
    {
        findInterviewIfExistsById(codeInterviewId); // if not found, it will throw an exception
        var userCodingInterview = stream(m_interviewServiceHelper.findCodingInterviewParticipantsById(codeInterviewId).spliterator(), false);
        var users = userCodingInterview.map(UserCodingInterviews::getUser).map(m_userMapper::toUserDTO).toList();
        return new MultipleResponseMessage<>(users.size(), "Participants found successfully", users);
    }

    /**
     * Get interview by project id
     *
     * @param projectId the project id
     * @return the response message
     */
    public ResponseMessage<Object> getInterviewByProjectId(UUID projectId)
    {
        var project = findProjectIfExistsById(projectId);
        var projectDTO = m_projectMapper.toProjectDTO(project);
        var codingInterviewDTO = m_codingInterviewMapper.toCodingInterviewDTO(project.getCodingInterview(), projectDTO);
        return new ResponseMessage<>("Interview found successfully", Status.OK, codingInterviewDTO);
    }

    /**
     * Get interview
     *
     * @param codeInterviewId the code interview id
     * @return the response message
     */
    public ResponseMessage<Object> getInterview(UUID codeInterviewId)
    {
        var interview = findInterviewIfExistsById(codeInterviewId);
        var projectDTO = m_projectMapper.toProjectDTO(interview.getProject());
        var codingInterviewDTO = m_codingInterviewMapper.toCodingInterviewDTO(interview, projectDTO);
        return new ResponseMessage<>("Interview found successfully", Status.OK, codingInterviewDTO);
    }

    /**
     * Start interview
     *
     * @param codeInterviewId the code interview id
     * @return the response message
     */
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


    /**
     * Find user interview information
     *
     * @param userId the user id
     * @return the response message
     */
    public MultipleResponseMessage<Object> findUserInterviewInformation(UUID userId)
    {
        findUserIfExistsById(userId);
        var projects = stream(m_interviewServiceHelper.findOwnerProjectsByUserId(userId).spliterator(), false);
        var projectsOwnerDTO = m_projectMapper.toOwnerProjectsDTO(projects.map(this::toOwnerProjectDTO).toList());
        return new MultipleResponseMessage<>(projectsOwnerDTO.ownerProjects().size(), "Projects are found!", projectsOwnerDTO);
    }

    /**
     * Convert project to owner project DTO
     *
     * @param project the project
     * @return the response message
     */
    private OwnerProjectDTO toOwnerProjectDTO(Project project)
    {
        var participantsDTO = m_projectMapper.toProjectsParticipantDTO(project.getProjectParticipants().stream().map(m_projectMapper::toProjectParticipantDTO).toList());
        return m_projectMapper.toOwnerProjectDTO(project, participantsDTO);
    }


    /**
     * Check if the user solved the interview before
     *
     * @param userId      the user id
     * @param interviewId the interview id
     * @return the response message
     */
    public ResponseMessage<Object> isUserSolvedBefore(UUID userId, UUID interviewId)
    {
        var userCodingInterview = m_interviewServiceHelper.findUserCodingInterviewByUserIdAndInterviewId(userId, interviewId);

        if (userCodingInterview == null)
            return new ResponseMessage<>("User not found", Status.NOT_FOUND, false);

        var interview = userCodingInterview.getCodingInterview();

        if (interview.getStartTime().isAfter(LocalDateTime.now()))
            return new ResponseMessage<>("Interview is not started yet", Status.NOT_ACCEPTED, false);

        if (interview.getEndTime().isBefore(LocalDateTime.now()))
            return new ResponseMessage<>("Interview is over", Status.NOT_ACCEPTED, false);

        var result = userCodingInterview.getInterviewStatus() == InterviewStatus.COMPLETED;

        return new ResponseMessage<>("User solved before", Status.OK, result);
    }

    /**
     * Accept or reject the interview
     *
     * @param id         the interview id
     * @param isAccepted the interview status
     * @return the response message
     */
    public ResponseMessage<Object> acceptInterview(UUID id, boolean isAccepted)
    {
        var userCodingInterview = m_interviewServiceHelper.findUserCodingInterviewByInterviewId(id);

        if (userCodingInterview.isEmpty())
            return new ResponseMessage<>("Interview not found", Status.NOT_FOUND, null);


        userCodingInterview.get().setInterviewResult(isAccepted ? InterviewResult.PASSED : InterviewResult.FAILED);
        m_interviewServiceHelper.createUserCodingInterviews(userCodingInterview.get());

        var codingInterview = userCodingInterview.get().getCodingInterview();
        var project = codingInterview.getProject();
        var user = userCodingInterview.get().getUser();
        var emailMsg = format("Hi %s! Your interview is %s!.", user.getUsername(), isAccepted ? "accepted" : "rejected");
        var dto = new InterviewResultDTO(project.getProjectOwner().getUserId(), user.getUserId(), project.getProjectName(), emailMsg, user.getEmail());
        var msg = format("Interview is %s!.", isAccepted ? "accepted" : "rejected");
        return new ResponseMessage<>(msg, Status.OK, dto);
    }

    // helper methods

    private CodingInterview findInterviewIfExistsById(UUID codeInterviewId)
    {
        return m_interviewServiceHelper.findCodingInterviewById(codeInterviewId).orElseThrow(() -> new DataServiceException("Interview not found"));
    }


    private User findUserIfExistsById(UUID userId)
    {
        return m_interviewServiceHelper.findUserById(userId).orElseThrow(() -> new DataServiceException("User not found"));
    }


    private Project findProjectIfExistsById(UUID projectId)
    {
        return m_interviewServiceHelper.findProjectById(projectId).orElseThrow(() -> new DataServiceException("Project not found"));
    }


    /**
     * Send notification
     *
     * @param owner   the owner
     * @param userId  the user id
     * @param message the message
     */
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

    /**
     * Send notification to the participants
     *
     * @param object the coding interview object
     * @param status the interview status
     */
    public void sendNotification(CodingInterviewDTO object, EInterviewStatus status)
    {
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


    /**
     * Send emails
     *
     * @param codingInterviewDTO the coding interview dto
     * @param list               the list
     * @param template           the template
     */
    public void sendEmails(CodingInterviewDTO codingInterviewDTO, List<UserEmailDTO> list, String template)
    {
        list.forEach(u -> sendEmail(codingInterviewDTO.codingInterviewId(), u.email(), codingInterviewDTO.projectDTO().projectName(), u.userId(), template));
    }

    /**
     * Convert local date time to string with format
     *
     * @param localDateTime the local date time
     * @return the string
     */
    private String toLocalDateTimeString(LocalDateTime localDateTime)
    {
        return localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy kk:mm:ss"));
    }

    /**
     * Send email
     *
     * @param interviewId  the interview id
     * @param email        the email
     * @param projectName  the project name
     * @param userId       the user id
     * @param templateName the template name
     */
    private void sendEmail(UUID interviewId, String email, String projectName, UUID userId, String templateName)
    {
        var template = Util.getEmailTemplate(templateName);
        var user = findUserIfExistsById(userId);
        var interview = findInterviewIfExistsById(interviewId);
        var startDate = toLocalDateTimeString(interview.getStartTime());
        var endDate = toLocalDateTimeString(interview.getEndTime());
        var emailStr = String.format(m_interviewEmail, interviewId, userId);
        var title = "Coding Interview Assigned for " + projectName;
        var msg = format(template, projectName, user.getUsername(), interview.getTitle(), startDate, endDate, emailStr);

        var topic = new EmailTopic(EmailType.ASSIGN_INTERVIEW, email, title, msg, null);
        m_kafkaProducer.sendEmail(topic);
    }
}
