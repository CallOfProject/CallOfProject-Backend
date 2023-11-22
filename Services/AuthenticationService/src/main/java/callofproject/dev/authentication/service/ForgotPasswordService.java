package callofproject.dev.authentication.service;

import callofproject.dev.authentication.dto.ForgotPasswordDTO;
import callofproject.dev.authentication.dto.MessageResponseDTO;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.repository.authentication.dal.UserServiceHelper;
import callofproject.dev.service.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.service.jwt.JwtUtil.generateToken;
import static java.lang.String.format;

@Service
@Lazy
public class ForgotPasswordService
{
    private final UserServiceHelper m_userServiceHelper;
    private final PasswordEncoder m_passwordEncoder;

    @Value("${authentication.url.forgot-password}")
    private String m_forgotPasswordUrl;

    public ForgotPasswordService(UserServiceHelper userServiceHelper, PasswordEncoder passwordEncoder)
    {
        m_userServiceHelper = userServiceHelper;
        m_passwordEncoder = passwordEncoder;
    }

    /**
     * Send Reset password link to email.
     *
     * @param email represent the email.
     * @return the boolean value.
     */
    public MessageResponseDTO<Object> sendResetPasswordLink(String email)
    {
        return doForDataService(() -> sendResetPasswordLinkCallback(email), "ForgotPasswordService::sendResetPasswordLink");
    }

    /**
     * Change password.
     *
     * @param forgotPasswordDTO represent the necessary information for change password.
     * @return the boolean value.
     */
    public MessageResponseDTO<Object> resetPassword(ForgotPasswordDTO forgotPasswordDTO)
    {
        return doForDataService(() -> resetPasswordCallback(forgotPasswordDTO), "ForgotPasswordService::resetPassword");
    }

    //---------------------------------------------------CALLBACK-------------------------------------------------------
    private MessageResponseDTO<Object> sendResetPasswordLinkCallback(String email)
    {
        var user = m_userServiceHelper.findByEmail(email);

        if (user.isEmpty())
            throw new DataServiceException("User not found!");

        var claims = new HashMap<String, Object>();
        claims.put("uuid", user.get().getUserId());
        var passwordResetToken = generateToken(claims, user.get().getUsername());

        var url = format(m_forgotPasswordUrl, passwordResetToken);
        System.out.println(url);
        // send email
        throw new UnsupportedOperationException("TODO:");
    }

    private MessageResponseDTO<Object> resetPasswordCallback(ForgotPasswordDTO forgotPasswordDTO)
    {
        try
        {
            var userId = JwtUtil.extractUuid(forgotPasswordDTO.user_token());

            var user = m_userServiceHelper.findById(UUID.fromString(userId));

            if (user.isEmpty())
                throw new DataServiceException("User Not Found!");

            if (!JwtUtil.isTokenValid(forgotPasswordDTO.user_token(), user.get().getUsername()))
                throw new DataServiceException("Link is not valid!");

            user.get().setPassword(m_passwordEncoder.encode(forgotPasswordDTO.new_password()));

            m_userServiceHelper.saveUser(user.get());

            return new MessageResponseDTO<>("Password changed Successfully!", 200, true);

        } catch (Exception ex)
        {
            throw new DataServiceException(ex.getMessage());
        }
    }
}
