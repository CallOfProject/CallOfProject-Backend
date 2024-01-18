package callofproject.dev.authentication.dto;

import java.util.List;


/**
 * Data Transfer Object for a list of users.
 */
public record UsersDTO(List<UserDTO> users)
{
}
