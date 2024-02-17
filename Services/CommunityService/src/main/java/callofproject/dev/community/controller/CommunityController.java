package callofproject.dev.community.controller;

import callofproject.dev.community.dto.CreateCommunityDTO;
import callofproject.dev.community.service.CommunityService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("api/community/project")
public class CommunityController
{
    private final CommunityService m_communityService;

    public CommunityController(CommunityService communityService)
    {
        m_communityService = communityService;
    }

    @PostMapping("/create")
    public void createCommunity(@RequestBody CreateCommunityDTO createCommunityDTO)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping("/add/participant")
    public void addParticipant(@RequestParam("community_id") UUID communityId, @RequestParam("user_id") UUID userId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @DeleteMapping("/remove/participant")
    public void removeParticipant(@RequestParam("community_id") UUID communityId, @RequestParam("user_id") UUID userId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @DeleteMapping("/delete")
    public void deleteCommunity(@RequestParam("community_id") UUID communityId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping("/get/participants")
    public void getParticipants(@RequestParam("community_id") UUID communityId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping("/get/communities")
    public void getCommunities(@RequestParam("user_id") UUID userId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping("/get/community")
    public void getCommunity(@RequestParam("community_id") UUID communityId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }
}
