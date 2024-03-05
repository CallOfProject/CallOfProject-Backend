package callofproject.dev.service.scheduler.service.callback;

import callofproject.dev.data.interview.dal.InterviewServiceHelper;
import callofproject.dev.data.interview.entity.CodingInterview;
import callofproject.dev.data.interview.entity.TestInterview;
import callofproject.dev.data.interview.entity.UserCodingInterviews;
import callofproject.dev.data.interview.entity.UserTestInterviews;
import callofproject.dev.data.interview.entity.enums.InterviewResult;
import callofproject.dev.data.interview.entity.enums.InterviewStatus;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

import static callofproject.dev.util.stream.StreamUtil.toStream;
import static java.time.LocalDateTime.now;

@Service
@Lazy
public class InterviewSchedulerServiceCallback
{
    private final InterviewServiceHelper m_serviceHelper;

    public InterviewSchedulerServiceCallback(InterviewServiceHelper interviewServiceHelper)
    {
        m_serviceHelper = interviewServiceHelper;
    }

    // Check for expired coding interviews and mark them as completed
    public void checkExpiredCodingInterviews()
    {
        var interviews = toStream(m_serviceHelper.findAllInterviews()).toList();
        checkCodingInterviews(interviews);
    }

    // Check for expired test interviews and mark them as completed
    public void checkExpiredTestInterviews()
    {
        checkTestInterviews(toStream(m_serviceHelper.findAllTestInterviews()).toList());
    }

    // -----------------------------------------------------------------------------------------------------------------
    private void checkTestInterviews(List<TestInterview> testInterviews)
    {
        var expiredTestInterviews = testInterviews.stream().filter(ci -> ci.getStartTime().isBefore(now())).toList();

        for (var ti : expiredTestInterviews)
        {
            ti.setDescription("Interview is time Expired");
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
            ci.setDescription("Interview is time Expired");
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
}
