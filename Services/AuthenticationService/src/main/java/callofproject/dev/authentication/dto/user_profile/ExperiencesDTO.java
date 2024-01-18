package callofproject.dev.authentication.dto.user_profile;

import java.util.List;

/**
 * Data Transfer Object for a list of experiences.
 */
public record ExperiencesDTO(List<ExperienceDTO> experiences)
{
}
