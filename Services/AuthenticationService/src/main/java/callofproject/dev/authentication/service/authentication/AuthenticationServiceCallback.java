package callofproject.dev.authentication.service.authentication;

import callofproject.dev.authentication.config.kafka.KafkaProducer;
import callofproject.dev.authentication.dto.UserKafkaDTO;
import callofproject.dev.authentication.dto.UserSignUpRequestDTO;
import callofproject.dev.authentication.dto.auth.AuthenticationRequest;
import callofproject.dev.authentication.dto.auth.AuthenticationResponse;
import callofproject.dev.authentication.dto.auth.RegisterRequest;
import callofproject.dev.authentication.service.usermanagement.UserManagementService;
import callofproject.dev.authentication.util.Util;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.data.common.scheduler.CountDownScheduler;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.repository.authentication.dal.UserServiceHelper;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.repository.authentication.enumeration.RoleEnum;
import callofproject.dev.repository.authentication.repository.rdbms.IUserRepository;
import callofproject.dev.service.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static callofproject.dev.data.common.enums.EOperation.REGISTER_NOT_VERIFY;
import static callofproject.dev.data.common.enums.EmailType.EMAIL_VERIFICATION;
import static callofproject.dev.service.jwt.JwtUtil.extractClaim;
import static callofproject.dev.service.jwt.JwtUtil.extractUsername;
import static java.lang.String.format;
import static java.time.LocalDateTime.parse;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

@Service
@Lazy
public class AuthenticationServiceCallback
{
    private final UserManagementService m_userManagementService;
    private final PasswordEncoder m_passwordEncoder;
    private final AuthenticationProvider m_authenticationProvider;
    private final KafkaProducer m_kafkaProducer;
    private final UserServiceHelper m_serviceHelper;
    @Value("${authentication.url.verify-user}")
    private String m_url;
    private final IUserRepository m_userRepository;
    @Value("${account.verification.time.minute.expire-time}")
    private int m_verifyUserTokenExpirationTime;
    private final ExecutorService m_executorService;

    /**
     * Constructor for AuthenticationServiceCallback.
     *
     * @param userManagementService  The UserManagementService to interact with user-related operations.
     * @param passwordEncoder        The PasswordEncoder for encoding passwords.
     * @param authenticationProvider The AuthenticationProvider for authenticating users.
     * @param kafkaProducer          The KafkaProducer for sending email notifications.
     * @param serviceHelper          The UserServiceHelper to interact with user-related operations.
     * @param userRepository         The IUserRepository to interact with user-related operations.
     * @param executorService        The ExecutorService for executing tasks.
     */
    public AuthenticationServiceCallback(UserManagementService userManagementService, PasswordEncoder passwordEncoder, AuthenticationProvider authenticationProvider, KafkaProducer kafkaProducer, UserServiceHelper serviceHelper, IUserRepository userRepository, ExecutorService executorService)
    {
        m_userManagementService = userManagementService;
        m_passwordEncoder = passwordEncoder;
        m_authenticationProvider = authenticationProvider;
        m_kafkaProducer = kafkaProducer;
        m_serviceHelper = serviceHelper;
        m_userRepository = userRepository;
        m_executorService = executorService;
    }


    /**
     * Register user with given RegisterRequest parameter.
     *
     * @param request represent the request.
     * @return AuthenticationResponse.
     */
    public AuthenticationResponse registerUserCallback(RegisterRequest request)
    {
        var dto = new UserSignUpRequestDTO(request.getEmail(), request.getFirst_name(),
                request.getMiddle_name(), request.getLast_name(), request.getUsername(),
                m_passwordEncoder.encode(request.getPassword()), request.getBirth_date(),
                RoleEnum.ROLE_USER);

        var user = m_userManagementService.saveUserForMobile(dto);

        return new AuthenticationResponse(user.getObject().accessToken(), user.getObject().refreshToken(), true, RoleEnum.ROLE_USER.getRole(), user.getObject().userId());
    }


    /**
     * Send authentication email.
     *
     * @param dto represent the UserSignUpRequestDTO.
     */
    public void sendAuthenticationEmail(UserSignUpRequestDTO dto)
    {
        var claims = createClaimsForRegister();
        var token = JwtUtil.generateToken(claims, dto.getUsername());

        var template = Util.getEmailTemplate("generic_template.html");
        var title = "Call-Of-Project Verification Link";
        var contentMessage = "Please click the link below to verify your account. If you did not register, please ignore this email.";
        var message = format(template, title, title, dto.getUsername(), contentMessage, format(m_url, token), "Verify Account");

        var emailTopic = new EmailTopic(EMAIL_VERIFICATION, dto.getEmail(), title, message, null);
        m_kafkaProducer.sendEmail(emailTopic);
        m_executorService.execute(() -> startTimerForUserVerification(dto.getUsername()));
    }


    /**
     * Start timer for user verification.
     *
     * @param username represent the username.
     */
    private void startTimerForUserVerification(String username)
    {
        var user = m_serviceHelper.findByUsername(username);

        if (user.isEmpty())
            throw new DataServiceException("User not found!");


        new CountDownScheduler(5, 1, TimeUnit.MINUTES)
        {
            @Override
            protected void onTick(long millisUntilFinished) throws Exception
            {
                // Doing nothing
            }

            @Override
            protected void onFinish() throws Exception
            {
                var user = m_serviceHelper.findByUsername(username);
                if (user.get().getIsAccountBlocked())
                {
                    m_userRepository.deleteById(user.get().getUserId());
                    m_kafkaProducer.sendMessage(new UserKafkaDTO(user.get().getUserId(), null, null, null, null, null,
                            REGISTER_NOT_VERIFY, null, null, null, -1, -1, -1, null));
                }
            }
        }.start();

    }

    /**
     * Create claims for register.
     *
     * @return HashMap<String, Object>
     */
    private HashMap<String, Object> createClaimsForRegister()
    {
        var claims = new HashMap<String, Object>();
        var dateTime = LocalDateTime.now();

        claims.put("endTime", dateTime
                .plusMinutes(m_verifyUserTokenExpirationTime)
                .format(ISO_DATE_TIME));

        return claims;
    }

    /**
     * Verify user and register.
     *
     * @param token represent the token.
     * @return ResponseMessage
     */
    public ResponseMessage<Object> verifyUserAndRegisterCallback(String token)
    {
        var username = extractUsername(token);
        var endTime = parse((String) extractClaim(token, date -> date.get("endTime")), ISO_DATE_TIME);

        var user = m_serviceHelper.findByUsername(username);

        if (endTime.isBefore(LocalDateTime.now()))
            return new ResponseMessage<>("Token expired!", Status.BAD_REQUEST, false);

        if (user.isEmpty())
            return new ResponseMessage<>("User not found!", Status.NOT_FOUND, false);

        if (!user.get().getIsAccountBlocked())
            return new ResponseMessage<>("user already verified!", Status.NOT_ACCEPTED, false);


        user.get().setAccountBlocked(false);
        m_serviceHelper.saveUser(user.get());

        return new ResponseMessage<>("User verified!", Status.OK, true);
    }

    /**
     * Login operation for admins.
     *
     * @param request represent the login information.
     * @return if success returns AuthenticationResponse that include token and status else return ErrorMessage.
     */
    public AuthenticationResponse authenticateCallback(AuthenticationRequest request)
    {
        if (request == null)
            throw new DataServiceException("AuthenticationRequest is null!");
        m_authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        var user = m_userManagementService.findUserByUsernameForAuthenticationService(request.username());

        if (user.getObject() == null)
            return new AuthenticationResponse(null, null, false, null, false,
                    null);

        if (user.getObject().getIsAccountBlocked() || user.getObject().getDeleteAt() != null)
            return new AuthenticationResponse(false, true);

        var topRole = findTopRole(user.getObject());
        var authorities = JwtUtil.populateAuthorities(user.getObject().getRoles());
        var claims = new HashMap<String, Object>();

        claims.put("authorities", authorities);

        var jwtToken = JwtUtil.generateToken(claims, user.getObject().getUsername());
        var refreshToken = JwtUtil.generateRefreshToken(claims, user.getObject().getUsername());

        return new AuthenticationResponse(jwtToken, refreshToken, true, topRole, false,
                user.getObject().getUserId());
    }

    /**
     * Find top role for user.
     *
     * @param user represent the user.
     * @return String value.
     */
    private String findTopRole(User user)
    {
        var role = RoleEnum.ROLE_USER.getRole();

        for (var r : user.getRoles())
        {
            if (r.getName().equals(RoleEnum.ROLE_ROOT.getRole()))
            {
                role = RoleEnum.ROLE_ROOT.getRole();
                break;
            }
            if (r.getName().equals(RoleEnum.ROLE_ADMIN.getRole()))
                role = RoleEnum.ROLE_ADMIN.getRole();
        }
        return role;
    }

    /**
     * Refresh token.
     *
     * @param request  represent the request.
     * @param response represent the response.
     * @return boolean value.
     * @throws IOException if an I/O error occurs.
     */
    public boolean refreshTokenCallback(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return false;

        refreshToken = authHeader.substring(7);
        username = extractUsername(refreshToken);
        if (username != null)
        {
            var user = m_userManagementService.findUserByUsernameForAuthenticationService(username);

            if (JwtUtil.isTokenValid(refreshToken, user.getObject().getUsername()))
            {
                var accessToken = JwtUtil.generateToken(user.getObject().getUsername());
                var topRole = findTopRole(user.getObject());
                var authResponse = new AuthenticationResponse(accessToken, refreshToken, true, topRole,
                        user.getObject().getIsAccountBlocked(), user.getObject().getUserId());

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                return true;
            }
        }
        return false;
    }

    /**
     * Validate given token.
     *
     * @param token represent the jwt.
     * @return boolean value.
     */
    public boolean validateTokenCallback(String token)
    {
        var username = extractUsername(token);

        if (username == null || username.isBlank() || username.isEmpty())
            throw new DataServiceException("User not found!");

        var user = m_userManagementService.findUserByUsername(username);

        return JwtUtil.isTokenValid(token, user.getObject().username());
    }


    /**
     * Find user by username.
     *
     * @param username represent the username.
     * @return User.
     */
    public Optional<User> findUserByUsernameCallback(String username)
    {
        return m_serviceHelper.findByUsername(username);
    }


}
