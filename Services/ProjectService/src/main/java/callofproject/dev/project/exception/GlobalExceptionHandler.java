package callofproject.dev.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler
{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex)
    {
        var errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleMessageNotReadableException(HttpMessageNotReadableException ex)
    {
        var errorMessage = ex.getMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }

}
