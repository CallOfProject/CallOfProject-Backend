package callofproject.dev.service.scheduler.service.callback;

import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.data.common.enums.NotificationDataType;
import callofproject.dev.data.common.enums.NotificationType;
import callofproject.dev.data.interview.dal.InterviewServiceHelper;
import callofproject.dev.data.interview.entity.CodingInterview;
import callofproject.dev.data.interview.entity.TestInterview;
import callofproject.dev.data.interview.entity.UserCodingInterviews;
import callofproject.dev.data.interview.entity.UserTestInterviews;
import callofproject.dev.data.interview.entity.enums.InterviewResult;
import callofproject.dev.data.interview.entity.enums.InterviewStatus;
import callofproject.dev.service.scheduler.config.kafka.KafkaProducer;
import callofproject.dev.service.scheduler.dto.NotificationKafkaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static callofproject.dev.data.common.enums.EmailType.REMAINDER;
import static callofproject.dev.service.scheduler.util.Util.getEmailTemplate;
import static callofproject.dev.util.stream.StreamUtil.toStream;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Service
@Lazy
public class InterviewSchedulerServiceCallback
{
    private final InterviewServiceHelper m_serviceHelper;
    private final KafkaProducer m_kafkaProducer;

    @Value("${interview.email.template.path}")
    private String m_emailTemplate;

    @Value("${interview.coding.url}")
    private String m_codingInterviewUrl;

    @Value("${interview.test.url}")
    private String m_testInterviewUrl;

    @Value("${interview.email.expired.title}")
    private String m_timeExpiredStr;

    @Value("${interview.email.coding.reminder.title}")
    private String m_codingInterviewReminderTitle;

    @Value("${interview.email.test.reminder.title}")
    private String m_testInterviewReminderTitle;

    @Value("${interview.reminder.message-template}")
    private String m_reminderMessageTemplate;

    @Value("${interview.reminder-date-from-now}")
    private int m_reminderDays;


    public InterviewSchedulerServiceCallback(InterviewServiceHelper interviewServiceHelper, KafkaProducer kafkaProducer)
    {
        m_serviceHelper = interviewServiceHelper;
        m_kafkaProducer = kafkaProducer;
    }

    // Check for expired coding interviews and mark them as completed
    public void checkExpiredCodingInterviews()
    {
        checkCodingInterviews(toStream(m_serviceHelper.findAllInterviews()).toList());
    }

    // Check for expired test interviews and mark them as completed
    public void checkExpiredTestInterviews()
    {
        checkTestInterviews(toStream(m_serviceHelper.findAllTestInterviews()).toList());
    }

    // Send reminder emails for coding interviews
    public void reminderCodingInterviews()
    {
        var interviews = m_serviceHelper.findAllInterviewsByEnDate(now().plusDays(m_reminderDays));

        for (var interview : interviews)
        {
            sendEmail(interview, m_codingInterviewReminderTitle);
            sendNotification(interview, m_codingInterviewReminderTitle);
        }
    }


    // Send reminder emails for test interviews
    public void reminderTestInterviews()
    {
        var interviews = m_serviceHelper.findAllTestInterviewsByEnDate(now().plusDays(m_reminderDays));

        for (var interview : interviews)
        {
            sendEmail(interview, m_testInterviewReminderTitle);
            sendNotification(interview, m_testInterviewReminderTitle);
        }
    }


    public void checkStartedTestInterviews()
    {
        var interviews = m_serviceHelper.finAllTestInterviewsByStartDate(now());

        for (var interview : interviews)
        {
            interview.setDescription("Interview has started");
            interview.setInterviewStatus(InterviewStatus.STARTED);
            m_serviceHelper.createInterview(interview);
            markTestInterviewParticipantsAsStarted(interview.getTestInterviews().stream().toList());
        }
        System.out.println("finished checkStartedTestInterviews");
    }


    public void checkStartedCodingInterviews()
    {
        var interviews = m_serviceHelper.finAllCodingInterviewsByStartDate(now());

        for (var interview : interviews)
        {
            interview.setDescription("Interview has started");
            interview.setInterviewStatus(InterviewStatus.STARTED);
            m_serviceHelper.createCodeInterview(interview);
            markCodingInterviewParticipantsAsStarted(interview.getCodingInterviews().stream().toList());
        }
    }


    // -----------------------------------------------------------------------------------------------------------------
    private void markTestInterviewParticipantsAsStarted(List<UserTestInterviews> list)
    {
        for (var uci : list)
        {
            uci.setInterviewStatus(InterviewStatus.STARTED);
            m_serviceHelper.createUserTestInterviews(uci);
        }
    }

    private void markCodingInterviewParticipantsAsStarted(List<UserCodingInterviews> list)
    {
        for (var uci : list)
        {
            uci.setInterviewStatus(InterviewStatus.STARTED);
            m_serviceHelper.createUserCodingInterviews(uci);
        }
    }

    private void checkTestInterviews(List<TestInterview> testInterviews)
    {
        var expiredTestInterviews = testInterviews.stream().filter(ci -> ci.getStartTime().isBefore(now())).toList();

        for (var ti : expiredTestInterviews)
        {
            ti.setDescription(m_timeExpiredStr);
            ti.setInterviewStatus(InterviewStatus.COMPLETED);
            m_serviceHelper.createInterview(ti);
            markTestInterviewParticipantsAsCompleted(ti.getTestInterviews().stream().toList());
        }
    }

    private void checkCodingInterviews(List<CodingInterview> codingInterviews)
    {
        var expiredCodingInterviews = codingInterviews.stream().filter(ci -> ci.getStartTime().isBefore(now())).toList();

        for (var ci : expiredCodingInterviews)
        {
            ci.setDescription(m_timeExpiredStr);
            ci.setInterviewStatus(InterviewStatus.COMPLETED);
            m_serviceHelper.createCodeInterview(ci);
            markCodingInterviewParticipantsAsCompleted(ci.getCodingInterviews().stream().toList());
        }
    }

    private void markCodingInterviewParticipantsAsCompleted(List<UserCodingInterviews> list)
    {
        for (var uci : list)
        {
            uci.setInterviewResult(InterviewResult.COMPLETED);
            uci.setInterviewStatus(InterviewStatus.COMPLETED);
            m_serviceHelper.createUserCodingInterviews(uci);
        }
    }

    private void markTestInterviewParticipantsAsCompleted(List<UserTestInterviews> list)
    {
        for (var uci : list)
        {
            uci.setInterviewResult(InterviewResult.COMPLETED);
            uci.setInterviewStatus(InterviewStatus.COMPLETED);
            m_serviceHelper.createUserTestInterviews(uci);
        }
    }

    private void sendEmail(CodingInterview interview, String title)
    {
        var userCodingInterviews = interview.getCodingInterviews().stream().toList();
        var template = getEmailTemplate(m_emailTemplate).trim();

        for (var p : userCodingInterviews)
        {
            var user = p.getUser();
            var interviewLink = format(m_codingInterviewUrl, interview.getCodingInterviewId(), user.getUserId());
            var message = format(template, user.getUsername(), interview.getProject().getProjectName(), interviewLink);
            m_kafkaProducer.sendEmail(new EmailTopic(REMAINDER, user.getEmail(), title, message, null));
        }
    }

    private void sendEmail(TestInterview interview, String title)
    {
        var userCodingInterviews = interview.getTestInterviews().stream().toList();
        var template = getEmailTemplate(m_emailTemplate).trim();

        for (var p : userCodingInterviews)
        {
            var user = p.getUser();
            var interviewLink = format(m_testInterviewUrl, interview.getId(), user.getUserId());
            var message = format(template, user.getUsername(), interview.getProject().getProjectName(), interviewLink);
            m_kafkaProducer.sendEmail(new EmailTopic(REMAINDER, user.getEmail(), title, message, null));
        }
    }

    private void sendNotification(CodingInterview interview, String title)
    {
        var userCodingInterviews = interview.getCodingInterviews().stream().toList();

        for (var codingInterview : userCodingInterviews)
        {
            var user = codingInterview.getUser();
            var owner = codingInterview.getCodingInterview().getProject().getProjectOwner();
            var interviewLink = format(m_codingInterviewUrl, interview.getCodingInterviewId(), user.getUserId());
            var msg = format(m_reminderMessageTemplate, interview.getTitle(), m_reminderDays);

            send(owner.getUserId(), user.getUserId(), title, msg, interviewLink);
        }
    }

    private void sendNotification(TestInterview interview, String title)
    {
        var userTestInterviews = interview.getTestInterviews().stream().toList();

        for (var testInterview : userTestInterviews)
        {
            var user = testInterview.getUser();
            var owner = testInterview.getTestInterview().getProject().getProjectOwner();
            var interviewLink = format(m_testInterviewUrl, interview.getId(), user.getUserId());
            var msg = format(m_reminderMessageTemplate, interview.getTitle(), m_reminderDays);

            send(owner.getUserId(), user.getUserId(), title, msg, interviewLink);
        }
    }

    private void send(UUID fromUserId, UUID toUserId, String title, String message, String link)
    {
        var notification = new NotificationKafkaDTO.Builder()
                .setFromUserId(fromUserId)
                .setToUserId(toUserId)
                .setNotificationTitle(title)
                .setMessage(message)
                .setNotificationType(NotificationType.INFORMATION)
                .setNotificationDataType(NotificationDataType.INTERVIEW)
                .setNotificationLink(link)
                .build();

        m_kafkaProducer.sendNotification(notification);
    }

}