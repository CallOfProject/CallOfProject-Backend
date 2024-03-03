package callofproject.dev.service.interview.controller;

import callofproject.dev.service.interview.dto.test.*;
import callofproject.dev.service.interview.service.testinterview.TestInterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/interview/test")
public class TestInterviewController
{
    private final TestInterviewService m_testInterviewService;

    public TestInterviewController(TestInterviewService testInterviewService)
    {
        m_testInterviewService = testInterviewService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTestInterview(@RequestBody CreateTestDTO dto)
    {
        return subscribe(() -> ok(m_testInterviewService.createInterview(dto)), ex -> internalServerError().body(ex.getMessage()));
    }


    @GetMapping("/find/question/by/interview-id")
    public ResponseEntity<Object> getQuestion(@RequestParam("interview_id") UUID interviewId, @RequestParam("q") int q)
    {
        return subscribe(() -> ok(m_testInterviewService.getQuestion(interviewId, q)), ex -> internalServerError().body(ex.getMessage()));
    }

    @PostMapping("/submit/answer/question")
    public ResponseEntity<Object> submitAnswer(@RequestBody QuestionAnswerDTO dto)
    {
        return subscribe(() -> ok(m_testInterviewService.submitAnswer(dto)), ex -> internalServerError().body(ex.getMessage()));
    }


    @PostMapping("add/question")
    public ResponseEntity<?> addQuestion(@RequestBody CreateQuestionDTO createQuestionDTO)
    {
        return subscribe(() -> ok(m_testInterviewService.addQuestion(createQuestionDTO)), ex -> internalServerError().body(ex.getMessage()));
    }

    @PostMapping("/assign")
    public ResponseEntity<Object> assignTestInterview(@RequestParam("interview_id") UUID interviewId, @RequestParam("user_id") UUID userId)
    {
        return subscribe(() -> ok(m_testInterviewService.assignTestInterview(interviewId, userId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @PostMapping("/assign/multiple")
    public ResponseEntity<Object> assignMultipleTestInterview(@RequestBody AssignMultipleInterviewDTO dto)
    {
        return subscribe(() -> ok(m_testInterviewService.assignMultipleTestInterview(dto)), ex -> internalServerError().body(ex.getMessage()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteTestInterview(@RequestParam("interview_id") UUID interviewId)
    {
        return subscribe(() -> ok(m_testInterviewService.deleteTestInterview(interviewId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @DeleteMapping("/delete/by/project-id")
    public ResponseEntity<Object> deleteTestInterviewByProjectId(@RequestParam("project_id") UUID projectId)
    {
        return subscribe(() -> ok(m_testInterviewService.deleteTestInterviewByProjectId(projectId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @DeleteMapping("delete/question")
    public ResponseEntity<Object> deleteQuestion(@RequestParam("question_id") long questionId)
    {
        return subscribe(() -> ok(m_testInterviewService.deleteQuestion(questionId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @GetMapping("/find/questions/all/by/interview-id")
    public ResponseEntity<Object> getQuestions(@RequestParam("interview_id") UUID interviewId)
    {
        return subscribe(() -> ok(m_testInterviewService.getQuestions(interviewId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @GetMapping("/find/questions/all/by/project-id")
    public ResponseEntity<Object> getQuestionsByProjectId(@RequestParam("project_id") UUID projectId)
    {
        return subscribe(() -> ok(m_testInterviewService.getQuestionsByProjectId(projectId)), ex -> internalServerError().body(ex.getMessage()));
    }


    @GetMapping("/find/question/by/project-id")
    public ResponseEntity<Object> getQuestionByProjectId(@RequestParam("project_id") UUID projectId, @RequestParam("q") int q)
    {
        return subscribe(() -> ok(m_testInterviewService.getQuestionByProjectId(projectId, q)), ex -> internalServerError().body(ex.getMessage()));
    }


    @PostMapping("/start")
    public ResponseEntity<Object> startTestInterview(@RequestParam("interview_id") UUID interviewId)
    {
        return subscribe(() -> ok(m_testInterviewService.startTestInterview(interviewId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @PostMapping("/start/by/project-id")
    public ResponseEntity<Object> startTestInterviewByProjectId(@RequestParam("project_id") UUID projectId)
    {
        return subscribe(() -> ok(m_testInterviewService.startTestInterviewByProjectId(projectId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @PostMapping("/finish")
    public ResponseEntity<Object> finishTestInterview(@RequestBody TestInterviewFinishDTO dto)
    {
        return subscribe(() -> ok(m_testInterviewService.finishTestInterview(dto)), ex -> internalServerError().body(ex.getMessage()));
    }

    @PostMapping("/submit")
    public ResponseEntity<Object> submitInterview(@RequestParam("interview_id") UUID testInterviewId, @RequestParam("user_id") UUID userId)
    {
        return subscribe(() -> ok(m_testInterviewService.submitInterview(userId, testInterviewId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @GetMapping("/is-solved-before")
    public ResponseEntity<Object> isUserSolvedBefore(@RequestParam("user_id") UUID userId, @RequestParam("interview_id") UUID interviewId)
    {
        return subscribe(() -> ok(m_testInterviewService.isUserSolvedBefore(userId, interviewId)), ex -> internalServerError().body(ex.getMessage()));
    }


    @GetMapping("/info/interview")
    public ResponseEntity<Object> getInterviewInformation(@RequestParam("interview_id") UUID interviewId)
    {
        return subscribe(() -> ok(m_testInterviewService.getInterviewInformation(interviewId)), ex -> internalServerError().body(ex.getMessage()));
    }
}