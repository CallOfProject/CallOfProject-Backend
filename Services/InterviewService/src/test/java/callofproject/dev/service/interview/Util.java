package callofproject.dev.service.interview;

import callofproject.dev.data.interview.entity.Project;
import callofproject.dev.data.interview.entity.User;
import callofproject.dev.service.interview.dto.coding.CreateCodingInterviewDTO;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class Util
{
    public static SecureRandom ms_random = new SecureRandom();

    private Util()
    {
    }


    public static Project createProject(User owner)
    {
        return new Project(UUID.randomUUID(), "Test Project -" + ms_random.nextInt(1_000_000), owner);
    }

    public static User createUser()
    {
        var number = new Random().nextInt(1_000_000);
        return new User(UUID.randomUUID(), "test-" + number, "test-" + number, "test-" + number,
                "test-" + number, "test-" + number, null);
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

    public static CreateCodingInterviewDTO createInvalidCodingInterviewDTO(UUID projectId, List<String> userIds)
    {
        return new CreateCodingInterviewDTO("Test Title",
                "Test Question",
                null,
                50,
                100,
                projectId,
                LocalDateTime.now(),
                LocalDateTime.now().plusWeeks(1),
                userIds);
    }
}
