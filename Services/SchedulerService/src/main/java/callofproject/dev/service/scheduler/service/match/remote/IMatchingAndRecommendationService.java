package callofproject.dev.service.scheduler.service.match.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-match-scheduler-service", url = "${service.user-match-scheduler-service.url}")
public interface IMatchingAndRecommendationService
{
    @GetMapping("/match/users/by-tags")
    List<String> recommendUsersByUserTags(@RequestParam("uid") String userId);

    @GetMapping("/match/users/by-education")
    List<String> recommendUsersByEducation(@RequestParam("uid") String userId);

    @GetMapping("/match/users/by-experience")
    List<String> recommendUsersByExperience(@RequestParam("uid") String userId);

    @GetMapping("/match/users")
    List<String> recommendUsersByTagsAndEducationAndExperience(@RequestParam("uid") String userId);

    @GetMapping("/suggest/projects")
    List<String> recommendProjectsByProjectTagAndUserTags(@RequestParam("uid") String userId);
}
