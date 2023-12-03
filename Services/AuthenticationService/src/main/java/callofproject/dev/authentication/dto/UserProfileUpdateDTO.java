package callofproject.dev.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;
import java.util.UUID;

public record UserProfileUpdateDTO(
        @JsonProperty("user_id")
        UUID userId,
        String cv,
        @JsonProperty("profile_photo")
        String profilePhoto,
        @JsonProperty("about_me")
        String aboutMe,
        @JsonProperty("education_ids")
        Set<UUID> educationIds,
        @JsonProperty("course_ids")
        Set<UUID> courseIds,
        @JsonProperty("experience_ids")
        Set<UUID> experienceIds,
        @JsonProperty("link_ids")
        Set<Long> linkIds)
{

}
