package callofproject.dev.authentication.service;

import callofproject.dev.authentication.config.kafka.KafkaProducer;
import callofproject.dev.authentication.dto.ForgotPasswordDTO;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.data.common.enums.EmailType;
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

/**
 * Service class for handling forgot password operations.
 */
@Service
@Lazy
public class ForgotPasswordService
{
    private final UserServiceHelper m_userServiceHelper;
    private final PasswordEncoder m_passwordEncoder;

    @Value("${authentication.url.forgot-password}")
    private String m_forgotPasswordUrl;
    private final KafkaProducer m_kafkaProducer;

    /**
     * Constructor for ForgotPasswordService.
     *
     * @param userServiceHelper The UserServiceHelper to interact with user-related operations.
     * @param passwordEncoder   The PasswordEncoder for encoding passwords.
     * @param kafkaProducer     The KafkaProducer for sending email notifications.
     */
    public ForgotPasswordService(UserServiceHelper userServiceHelper, PasswordEncoder passwordEncoder, KafkaProducer kafkaProducer)
    {
        m_userServiceHelper = userServiceHelper;
        m_passwordEncoder = passwordEncoder;
        m_kafkaProducer = kafkaProducer;
    }

    /**
     * Sends a reset password link to the user's email.
     *
     * @param email The user's email address.
     * @return A ResponseMessage indicating the result of sending the reset password link.
     */
    public ResponseMessage<Object> sendResetPasswordLink(String email)
    {
        return doForDataService(() -> sendResetPasswordLinkCallback(email), "ForgotPasswordService::sendResetPasswordLink");
    }

    /**
     * Resets the user's password based on the provided ForgotPasswordDTO.
     *
     * @param forgotPasswordDTO The DTO containing the necessary information for password reset.
     * @return A ResponseMessage indicating the result of the password reset.
     */
    public ResponseMessage<Object> resetPassword(ForgotPasswordDTO forgotPasswordDTO)
    {
        return doForDataService(() -> resetPasswordCallback(forgotPasswordDTO), "ForgotPasswordService::resetPassword");
    }

    //------------------------------------------------------------------------------------------------------------------
    //####################################################-CALLBACKS-###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Sends a reset password link to the user's email.
     *
     * @param email The user's email address.
     * @return A ResponseMessage indicating the result of sending the reset password link.
     */
    private ResponseMessage<Object> sendResetPasswordLinkCallback(String email)
    {
        var user = m_userServiceHelper.findByEmail(email);

        if (user.isEmpty())
            throw new DataServiceException("User not found!");

        var claims = new HashMap<String, Object>();
        claims.put("uuid", user.get().getUserId());
        var passwordResetToken = generateToken(claims, user.get().getUsername());

        var url = format(m_forgotPasswordUrl, passwordResetToken);
        var message = format("Hello %s, \n\nYou can reset your password by clicking the link below: \n%s", user.get().getUsername(), url);
        var emailTopic = new EmailTopic(EmailType.PASSWORD_RESET, user.get().getEmail(), "Reset Password", message, null);
        m_kafkaProducer.sendEmail(emailTopic);
        return new ResponseMessage<>("Reset password link sent to your email!", 200, true);
    }

    /**
     * Resets the password for a user based on the provided ForgotPasswordDTO.
     *
     * @param forgotPasswordDTO The DTO containing the user's token and new password.
     * @return A ResponseMessage indicating the result of the password reset.
     * @throws DataServiceException If an error occurs during the password reset process.
     */
    private ResponseMessage<Object> resetPasswordCallback(ForgotPasswordDTO forgotPasswordDTO)
    {
        try
        {
            var userId = JwtUtil.extractUuid(forgotPasswordDTO.user_token());

            var user = m_userServiceHelper.findById(UUID.fromString(userId));

            if (user.isEmpty())
                throw new DataServiceException("User Not Found!");

            if (!JwtUtil.isTokenValid(forgotPasswordDTO.user_token(), user.get().getUsername()))
                throw new DataServiceException("Link is not valid!");

            var encodedPassword = m_passwordEncoder.encode(forgotPasswordDTO.new_password());

            user.get().setPassword(encodedPassword);
            m_userServiceHelper.saveUser(user.get());

            return new ResponseMessage<>("Password changed Successfully!", 200, true);

        } catch (Exception ex)
        {
            throw new DataServiceException(ex.getMessage());
        }
    }
}
