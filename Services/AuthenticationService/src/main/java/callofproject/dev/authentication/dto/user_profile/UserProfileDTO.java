package callofproject.dev.authentication.dto.user_profile;

import callofproject.dev.authentication.dto.UserTagDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Data Transfer Object for a user profile.
 */
public class UserProfileDTO
{
    public String cv;
    @JsonProperty("profile_photo")
    public String profilePhoto;
    @JsonProperty("about_me")
    public String aboutMe;
    @JsonProperty("user_rate")
    public double userRate;
    @JsonProperty("user_feedback_rate")
    public double userFeedbackRate;
    public List<CourseDTO> courses;
    public List<EducationDTO> educations;
    public List<ExperienceDTO> experiences;
    public List<LinkDTO> links;
    public List<UserTagDTO> tags;

    /**
     * Constructor.
     */
    public UserProfileDTO()
    {
    }
}
