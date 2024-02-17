package callofproject.dev.community.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record CreateCommunityDTO(
        @JsonProperty("project_owner_id")
        UUID projectOwnerId,
        @JsonProperty("project_id")
        UUID projectId,
        @JsonProperty("community_name")
        String communityName)
{
}
