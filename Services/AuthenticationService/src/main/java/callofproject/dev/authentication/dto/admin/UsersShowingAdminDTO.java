package callofproject.dev.authentication.dto.admin;

import java.util.List;


/**
 * Data Transfer Object for a course.
 */
public record UsersShowingAdminDTO(List<UserShowingAdminDTO> users)
{
}