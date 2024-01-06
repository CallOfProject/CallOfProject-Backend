package callofproject.dev.authentication.controller;

import callofproject.dev.authentication.dto.ForgotPasswordDTO;
import callofproject.dev.authentication.service.ForgotPasswordService;
import callofproject.dev.data.common.clas.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

@RequestMapping("api/forgot-password")
@RestController
public class ForgotPasswordController
{
    private final ForgotPasswordService m_forgotPasswordService;

    public ForgotPasswordController(ForgotPasswordService forgotPasswordService)
    {
        m_forgotPasswordService = forgotPasswordService;
    }

    /**
     * Send password reset email with generated token.
     *
     * @param email represent the email
     * @return the boolean value.
     */
    @PostMapping("/password-reset-request")
    public ResponseEntity<Object> SendPasswordResetEmail(@RequestParam("email") String email)
    {
        return subscribe(() -> ok(m_forgotPasswordService.sendResetPasswordLink(email)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Change user password after confirm
     *
     * @param forgotPasswordDTO represent the information of new password and token
     * @return boolean value.
     */
    @PostMapping("/password-reset")
    public ResponseEntity<Object> changePassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO)
    {
        return subscribe(() -> ok(m_forgotPasswordService.resetPassword(forgotPasswordDTO)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }
}
