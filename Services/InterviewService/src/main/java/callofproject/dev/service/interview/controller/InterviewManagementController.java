package callofproject.dev.service.interview.controller;

import callofproject.dev.service.interview.service.management.InterviewManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/interview/management")
public class InterviewManagementController
{
    private final InterviewManagementService m_managementService;

    public InterviewManagementController(InterviewManagementService managementService)
    {
        m_managementService = managementService;
    }


    @GetMapping("/find/all/by/user-id")
    public ResponseEntity<Object> findAllInterviewsByUserId(@RequestParam("user_id") UUID userId)
    {
        return subscribe(() -> ok(m_managementService.findAllInterviewsByUserId(userId)), ex -> internalServerError().body(ex.getMessage()));
    }


    @GetMapping("/find/coding-interview/owner")
    public ResponseEntity<Object> findCodingInterviewOwner(@RequestParam("interview_id") UUID interviewId)
    {
        return subscribe(() -> ok(m_managementService.findCodingInterviewOwner(interviewId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @GetMapping("/find/test-interview/owner")
    public ResponseEntity<Object> findTestInterviewOwner(@RequestParam("interview_id") UUID interviewId)
    {
        return subscribe(() -> ok(m_managementService.findTestInterviewOwner(interviewId)), ex -> internalServerError().body(ex.getMessage()));
    }
}
