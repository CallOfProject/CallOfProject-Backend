package callofproject.dev.service.scheduler.controller;

import callofproject.dev.service.scheduler.service.match.UserMatchingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/scheduler/user-match")
public class UserMatchingController
{
    private final UserMatchingService m_userMatchingService;

    public UserMatchingController(UserMatchingService userMatchingService)
    {
        m_userMatchingService = userMatchingService;
    }

/*    @RequestMapping("/match-users/by-tags")
    public List<String> recommendUsersByUserTags(String userId)
    {
        return m_userMatchingService.recommendUsersByUserTags(userId);
    }*/
}
