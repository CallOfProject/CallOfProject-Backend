package callofproject.dev.community.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record CommunityDTO(
        @JsonProperty("community_id")
        UUID communityId,
        @JsonProperty("community_name")
        String communityName,
        @JsonProperty("project_name")
        String projectName,
        @JsonProperty("project_owner")
        UserDTO projectOwner)
{
}
