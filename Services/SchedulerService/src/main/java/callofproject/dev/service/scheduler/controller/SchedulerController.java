package callofproject.dev.service.scheduler.controller;

import callofproject.dev.service.scheduler.service.SchedulerServiceFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/scheduler")
public class SchedulerController
{
    private final SchedulerServiceFacade m_schedulerServiceFacade;

    public SchedulerController(SchedulerServiceFacade schedulerServiceFacade)
    {
        m_schedulerServiceFacade = schedulerServiceFacade;
    }

    //api/scheduler/interview/test/check/started
    @GetMapping("/interview/test/check/started")
    public ResponseEntity<String> checkStartedTestInterviews()
    {
        return ResponseEntity.ok(m_schedulerServiceFacade.checkStartedTestInterviews());
    }

    @GetMapping("/interview/coding/check/started")
    public ResponseEntity<String> checkStartedCodingInterviews()
    {
        return ResponseEntity.ok(m_schedulerServiceFacade.checkStartedCodingInterviews());
    }

    @GetMapping("/interview/test/check/expired")
    public ResponseEntity<String> checkExpiredTestInterviews()
    {
        return ResponseEntity.ok(m_schedulerServiceFacade.checkExpiredTestInterviews());
    }

    @GetMapping("/interview/coding/check/expired")
    public ResponseEntity<String> checkExpiredCodingInterviews()
    {
        return ResponseEntity.ok(m_schedulerServiceFacade.checkExpiredCodingInterviews());
    }

    @GetMapping("/interview/test/reminder")
    public ResponseEntity<String> reminderTestInterviews()
    {
        return ResponseEntity.ok(m_schedulerServiceFacade.reminderTestInterviews());
    }

    @GetMapping("/interview/coding/reminder")
    public ResponseEntity<String> reminderCodingInterviews()
    {
        return ResponseEntity.ok(m_schedulerServiceFacade.reminderCodingInterviews());
    }

    @GetMapping("task/notify")
    public ResponseEntity<String> notifyUsersForTask()
    {
        return ResponseEntity.ok(m_schedulerServiceFacade.notifyUsersForTask());
    }

    @GetMapping("task/check/expired")
    public ResponseEntity<String> closeExpiredTasks()
    {
        return ResponseEntity.ok(m_schedulerServiceFacade.closeExpiredTasks());
    }

    @GetMapping("project/check/feedback-timeout")
    public ResponseEntity<String> checkFeedbackTimeout()
    {
        return ResponseEntity.ok(m_schedulerServiceFacade.checkFeedbackTimeout());
    }

    @GetMapping("project/check/deadline")
    public ResponseEntity<String> checkProjectDeadlines()
    {
        return ResponseEntity.ok(m_schedulerServiceFacade.checkProjectDeadlines());
    }

    @GetMapping("project/check/feedback")
    public ResponseEntity<String> checkFeedbacks()
    {
        return ResponseEntity.ok(m_schedulerServiceFacade.checkFeedbacks());
    }

    @GetMapping("project/check/start-date")
    public ResponseEntity<String> checkProjectStartDates()
    {
        return ResponseEntity.ok(m_schedulerServiceFacade.checkProjectStartDates());
    }

    @GetMapping("recommendation/projects/by-tags")
    public ResponseEntity<String> recommendProjects()
    {
        return ResponseEntity.ok(m_schedulerServiceFacade.recommendProjects());
    }

    @GetMapping("match/users/by-tags")
    public ResponseEntity<String> matchUsersByTags()
    {
        return ResponseEntity.ok(m_schedulerServiceFacade.matchUsersByTags());
    }

    @GetMapping("match/users/by-education")
    public ResponseEntity<String> matchUsersByEducation()
    {
        return ResponseEntity.ok(m_schedulerServiceFacade.matchUsersByEducation());
    }

    @GetMapping("match/users/by-experience")
    public ResponseEntity<String> matchUsersByExperience()
    {
        return ResponseEntity.ok(m_schedulerServiceFacade.matchUsersByExperience());
    }
}
