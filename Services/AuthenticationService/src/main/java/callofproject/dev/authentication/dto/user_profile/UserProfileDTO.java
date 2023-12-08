package callofproject.dev.authentication.dto.user_profile;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserProfileDTO
{
    public String cv;
    @JsonProperty("profile_photo")
    public String profilePhoto;
    @JsonProperty("about_me")
    public String aboutMe;
    @JsonProperty("user_rates")
    public UserRateDTO userRateDTO;
    public List<CourseDTO> courses;
    public List<EducationDTO> educations;
    public List<ExperienceDTO> experiences;
    public List<LinkDTO> links;
}
