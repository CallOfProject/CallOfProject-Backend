package callofproject.dev.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for the application.
 * This class handles various types of exceptions and returns the appropriate ResponseEntity.
 */
@ControllerAdvice
public class GlobalExceptionHandler
{

    /**
     * Default constructor.
     */
    public GlobalExceptionHandler()
    {
    }

    /**
     * Handles exceptions for invalid method arguments.
     *
     * @param ex MethodArgumentNotValidException exception
     * @return ResponseEntity containing the error message and BAD_REQUEST status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex)
    {
        var errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }

    /**
     * Handles exceptions when the HTTP message is not readable.
     *
     * @param ex HttpMessageNotReadableException exception
     * @return ResponseEntity containing the error message and BAD_REQUEST status
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleMessageNotReadableException(HttpMessageNotReadableException ex)
    {
        var errorMessage = ex.getMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }

    /**
     * Handles access denied exceptions in the application.
     *
     * @param ex      AccessDeniedException exception
     * @param request WebRequest instance
     * @return ResponseEntity with FORBIDDEN status and the error message
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request)
    {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erişim reddedildi: " + ex.getMessage());
    }
}
