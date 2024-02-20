package callofproject.dev.service.interview.controller;

import callofproject.dev.service.interview.dto.coding.TestInterviewSubmitAnswerDTO;
import callofproject.dev.service.interview.dto.test.AssignMultipleInterviewDTO;
import callofproject.dev.service.interview.dto.test.CreateQuestionDTO;
import callofproject.dev.service.interview.dto.test.CreateTestDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewFinishDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/interview/test")
public class TestInterviewController
{

    @PostMapping("/create")
    public ResponseEntity<Object> createTestInterview(@RequestBody CreateTestDTO dto)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping("add/question")
    public ResponseEntity<Object> addQuestion(@RequestBody CreateQuestionDTO createQuestionDTO)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping("/assign")
    public ResponseEntity<Object> assignTestInterview(@RequestParam("interview_id") String interviewId, @RequestParam("user_id") String userId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping("/assign/multiple")
    public ResponseEntity<Object> assignMultipleTestInterview(@RequestBody AssignMultipleInterviewDTO dto)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteTestInterview(@RequestParam("interview_id") String interviewId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @DeleteMapping("delete/question")
    public ResponseEntity<Object> deleteQuestion(@RequestParam("question_id") String questionId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping("/find/questions/all")
    public ResponseEntity<Object> getQuestions(@RequestParam("interview_id") String interviewId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping("/find/question/queue")
    public ResponseEntity<Object> getQuestion(@RequestParam("interview_id") String interviewId, @RequestParam("q") int q)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping("/submit/answer")
    public ResponseEntity<Object> submitAnswer(@RequestBody TestInterviewSubmitAnswerDTO dto)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping("/start")
    public ResponseEntity<Object> startTestInterview(@RequestParam("interview_id") String interviewId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping("/finish")
    public ResponseEntity<Object> finishTestInterview(@RequestBody TestInterviewFinishDTO dto)
    {
        throw new UnsupportedOperationException("Not implemented");
    }
}