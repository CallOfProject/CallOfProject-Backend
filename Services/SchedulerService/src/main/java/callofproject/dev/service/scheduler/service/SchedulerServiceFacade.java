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
            System.out.println("Checking started test interviews");
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
            System.out.println("Checking started coding interviews");
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
            System.out.println("Checking expired test interviews");
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
            System.out.println("Checking expired coding interviews");
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
            System.out.println("Checking reminder test interviews");
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
            System.out.println("Checking reminder coding interviews");
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
            System.out.println("Checking notify users for task");
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
            System.out.println("Checking close expired tasks");
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
            System.out.println("Checking feedback timeout");
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
            System.out.println("Checking project deadlines");
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
            System.out.println("Checking feedbacks");
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
            System.out.println("Checking project start dates");
            m_projectSchedulerService.checkProjectStartDates();
            return "'Check project start dates' scheduler is started.";
        } catch (Throwable ex)
        {
            return ex.getMessage();
        }
    }
}
