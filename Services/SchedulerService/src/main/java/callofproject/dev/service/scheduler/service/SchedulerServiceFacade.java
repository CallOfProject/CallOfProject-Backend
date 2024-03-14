package callofproject.dev.service.scheduler.service;

import callofproject.dev.service.scheduler.service.callback.InterviewSchedulerServiceCallback;
import callofproject.dev.service.scheduler.service.callback.ProjectSchedulerServiceCallback;
import callofproject.dev.service.scheduler.service.callback.TaskSchedulerServiceCallback;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class SchedulerServiceFacade
{
    private final InterviewSchedulerServiceCallback m_interviewSchedulerService;
    private final TaskSchedulerServiceCallback m_taskSchedulerService;
    private final ProjectSchedulerServiceCallback m_projectSchedulerService;

    public SchedulerServiceFacade(InterviewSchedulerServiceCallback interviewSchedulerService, TaskSchedulerServiceCallback taskSchedulerService, ProjectSchedulerServiceCallback projectSchedulerService)
    {
        m_interviewSchedulerService = interviewSchedulerService;
        m_taskSchedulerService = taskSchedulerService;
        m_projectSchedulerService = projectSchedulerService;
    }

    public String checkStartedTestInterviews()
    {
        try
        {
            m_interviewSchedulerService.checkStartedTestInterviews();
            return "'Started test interviews' scheduler is started.";
        } catch (Throwable ex)
        {
            return ex.getMessage();
        }
    }

    public String checkStartedCodingInterviews()
    {
        try
        {
            m_interviewSchedulerService.checkStartedCodingInterviews();
            return "'Started coding interviews' scheduler is started.";
        } catch (Throwable ex)
        {
            return ex.getMessage();
        }
    }

    public String checkExpiredTestInterviews()
    {
        try
        {
            m_interviewSchedulerService.checkExpiredTestInterviews();
            return "'Expired test interviews' scheduler is started.";
        } catch (Throwable ex)
        {
            return ex.getMessage();
        }
    }

    public String checkExpiredCodingInterviews()
    {
        try
        {
            m_interviewSchedulerService.checkExpiredCodingInterviews();
            return "'Expired coding interviews' scheduler is started.";
        } catch (Throwable ex)
        {
            return ex.getMessage();
        }
    }

    public String reminderTestInterviews()
    {
        try
        {
            m_interviewSchedulerService.reminderTestInterviews();
            return "'Reminder test interviews' scheduler is started.";
        } catch (Throwable ex)
        {
            return ex.getMessage();
        }
    }

    public String reminderCodingInterviews()
    {
        try
        {
            m_interviewSchedulerService.reminderCodingInterviews();
            return "'Reminder coding interviews' scheduler is started.";
        } catch (Throwable ex)
        {
            return ex.getMessage();
        }
    }


    public String notifyUsersForTask()
    {
        try
        {
            m_taskSchedulerService.runNotifyUsersForTask();
            return "'Notify users for task' scheduler is started.";
        } catch (Throwable ex)
        {
            return ex.getMessage();
        }
    }


    public String closeExpiredTasks()
    {
        try
        {
            m_taskSchedulerService.runCloseExpiredTasks();
            return "'Close expired tasks' scheduler is started.";
        } catch (Throwable ex)
        {
            return ex.getMessage();
        }
    }

    public String checkFeedbackTimeout()
    {
        try
        {
            m_projectSchedulerService.checkFeedbackTimeout();
            return "'Check feedback timeout' scheduler is started.";
        } catch (Throwable ex)
        {
            return ex.getMessage();
        }
    }

    public String checkProjectDeadlines()
    {
        try
        {
            m_projectSchedulerService.checkProjectDeadlines();
            return "'Check project deadlines' scheduler is started.";
        } catch (Throwable ex)
        {
            return ex.getMessage();
        }
    }


    public String checkFeedbacks()
    {
        try
        {
            m_projectSchedulerService.checkFeedbacks();
            return "'Check feedbacks' scheduler is started.";
        } catch (Throwable ex)
        {
            return ex.getMessage();
        }
    }

    public String checkProjectStartDates()
    {

        try
        {
            m_projectSchedulerService.checkProjectStartDates();
            return "'Check project start dates' scheduler is started.";
        } catch (Throwable ex)
        {
            return ex.getMessage();
        }
    }
}
