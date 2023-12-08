package callofproject.dev.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UserProfileUpdateDTO(
        @JsonProperty("updated_user_id")
        UUID userId,
        String cv,
        @JsonProperty("profile_photo")
        String profilePhoto,
        @JsonProperty("about_me")
        String aboutMe)
{

}
