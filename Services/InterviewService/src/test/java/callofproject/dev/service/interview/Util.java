package callofproject.dev.service.interview;

import callofproject.dev.data.interview.entity.Project;
import callofproject.dev.data.interview.entity.User;
import callofproject.dev.service.interview.dto.coding.CreateCodingInterviewDTO;
import callofproject.dev.service.interview.dto.test.CreateQuestionDTO;
import callofproject.dev.service.interview.dto.test.CreateTestDTO;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static java.lang.String.format;

public final class Util
{
    public static SecureRandom ms_random = new SecureRandom();

    private Util()
    {
    }

    public static User createUser(String username)
    {
        return new User(UUID.randomUUID(), username, format("%s@gmail.com", username), format("firstname-%s", username),
                format("middlename-%s", username), format("lastname-%s", username), null);
    }

    public static CreateCodingInterviewDTO createCodingInterviewDTO(UUID projectId, List<String> userIds)
    {
        return new CreateCodingInterviewDTO("Test Title",
                "Test Question",
                "Test Description",
                50,
                100,
                projectId,
                LocalDateTime.now(),
                LocalDateTime.now().plusWeeks(1),
                userIds);
    }

    public static CreateTestDTO createTestInterviewDTO(UUID projectId, List<String> userIds)
    {
        var question = new CreateQuestionDTO("test question", "A", "B", "C", "D", "B", 100);
        return new CreateTestDTO("Test Title",
                1,
                "Test Question",
                100,
                100,
                projectId,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                List.of(question),
                userIds);
    }
}
