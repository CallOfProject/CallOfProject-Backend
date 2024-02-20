package callofproject.dev.service.interview.controller;

import callofproject.dev.service.interview.dto.coding.CreateCodingInterviewDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interview/coding")
public class CodingInterviewController
{
    @PostMapping("/create")
    public ResponseEntity<Object> createCodeInterview(@RequestBody CreateCodingInterviewDTO dto)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PostMapping("/add/participant")
    public ResponseEntity<Object> addParticipant(@RequestBody String codeInterviewId, @RequestBody String userId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PostMapping("/remove/participant")
    public ResponseEntity<Object> removeParticipant(@RequestBody String codeInterviewId, @RequestBody String userId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
