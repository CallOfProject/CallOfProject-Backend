package callofproject.dev.service.interview.controller;

import callofproject.dev.service.interview.dto.coding.CreateCodingInterviewDTO;
import callofproject.dev.service.interview.service.codinginterview.ICodingInterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/interview/coding")
public class CodingInterviewController
{
    private final ICodingInterviewService m_codingInterviewService;

    public CodingInterviewController(ICodingInterviewService codingInterviewService)
    {
        m_codingInterviewService = codingInterviewService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createCodeInterview(@RequestBody CreateCodingInterviewDTO dto)
    {
        return subscribe(() -> ok(m_codingInterviewService.createCodeInterview(dto)), ex -> internalServerError().body(ex.getMessage()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCodeInterview(@RequestParam("owner_id") UUID ownerId, @RequestParam("interview_id") UUID codeInterviewId)
    {
        return subscribe(() -> ok(m_codingInterviewService.deleteCodeInterview(ownerId, codeInterviewId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @DeleteMapping("/delete/by/project-id")
    public ResponseEntity<?> deleteCodeInterviewByProjectId(@RequestParam("project_id") UUID projectId)
    {
        return subscribe(() -> ok(m_codingInterviewService.deleteCodeInterviewByProjectId(projectId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @PostMapping("/add/participant")
    public ResponseEntity<Object> addParticipant(@RequestParam("interview_id") UUID codeInterviewId, @RequestParam("user_id") UUID userId)
    {
        return subscribe(() -> ok(m_codingInterviewService.addParticipant(codeInterviewId, userId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @PostMapping("/add/participant/by/project-id")
    public ResponseEntity<Object> addParticipantByProjectId(@RequestParam("project_id") UUID projectId, @RequestParam("user_id") UUID userId)
    {
        return subscribe(() -> ok(m_codingInterviewService.addParticipantByProjectId(projectId, userId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @DeleteMapping("/remove/participant")
    public ResponseEntity<Object> removeParticipant(@RequestParam("interview_id") UUID codeInterviewId, @RequestParam("user_id") UUID userId)
    {
        return subscribe(() -> ok(m_codingInterviewService.removeParticipant(codeInterviewId, userId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @DeleteMapping("/remove/participant/by/project-id")
    public ResponseEntity<Object> removeParticipantByProjectId(@RequestParam("project_id") UUID projectId, @RequestParam("user_id") UUID userId)
    {
        return subscribe(() -> ok(m_codingInterviewService.removeParticipantByProjectId(projectId, userId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @GetMapping("/find/participants")
    public ResponseEntity<Object> getParticipants(@RequestParam("interview_id") UUID codeInterviewId)
    {
        return subscribe(() -> ok(m_codingInterviewService.getParticipants(codeInterviewId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @GetMapping("/find/participants/by/project-id")
    public ResponseEntity<Object> getParticipantsByProjectId(@RequestParam("project_id") UUID projectId)
    {
        return subscribe(() -> ok(m_codingInterviewService.getParticipantsByProjectId(projectId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @GetMapping("/find/by/project-id")
    public ResponseEntity<Object> getInterviewByProjectId(@RequestParam("project_id") UUID projectId)
    {
        return subscribe(() -> ok(m_codingInterviewService.getInterviewByProjectId(projectId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @GetMapping("/find/by/interview-id")
    public ResponseEntity<Object> getInterview(@RequestParam("interview_id") UUID codeInterviewId)
    {
        return subscribe(() -> ok(m_codingInterviewService.getInterview(codeInterviewId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @PostMapping("/submit")
    public ResponseEntity<Object> submitInterview(@RequestParam("interview_id") UUID codeInterviewId, @RequestParam("user_id") UUID userId, @RequestParam MultipartFile file)
    {
        return subscribe(() -> ok(m_codingInterviewService.submitInterview(userId, codeInterviewId, file)), ex -> internalServerError().body(ex.getMessage()));
    }

    @GetMapping("/find/info")
    public ResponseEntity<Object> findUserInterviewInformation(@RequestParam("user_id") UUID userId)
    {
        return subscribe(() -> ok(m_codingInterviewService.findUserInterviewInformation(userId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @GetMapping("/is-solved-before")
    public ResponseEntity<Object> isUserSolvedBefore(@RequestParam("user_id") UUID userId, @RequestParam("interview_id") UUID interviewId)
    {
        return subscribe(() -> ok(m_codingInterviewService.isUserSolvedBefore(userId, interviewId)), ex -> internalServerError().body(ex.getMessage()));
    }

}