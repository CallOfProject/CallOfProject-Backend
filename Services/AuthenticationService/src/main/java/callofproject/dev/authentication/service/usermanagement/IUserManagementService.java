package callofproject.dev.authentication.service.usermanagement;

import callofproject.dev.authentication.dto.*;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.repository.authentication.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface IUserManagementService
{
    ResponseMessage<UserSaveDTO> saveUser(UserSignUpRequestDTO userDTO);

    ResponseMessage<UserDTO> findUserByUsername(String username);

    UserResponseDTO<User> findUserByUsernameForAuthenticationService(String username);

    MultipleResponseMessagePageable<Object> findAllUsersPageableByContainsWord(int page, String word);

    ResponseMessage<Object> upsertUserProfile(UserProfileUpdateDTO dto, MultipartFile file, MultipartFile cv);

    ResponseMessage<Object> findUserProfileByUserId(UUID userId);

    ResponseMessage<Object> findUserProfileByUsername(String username);

    ResponseMessage<Object> findUserWithProfile(UUID userId);
}
