package stock.chart.security.exception;


import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccessTokenControllerAdvice {


    public List<FieldError> makeListErrors(String objectName, String field, String message) {
        return List.of(
            new FieldError(objectName, field, message)
        );
    }

    @ExceptionHandler(AccessTokenInvalidException.class)
    public ResponseEntity accessTokenInvalidException(AccessTokenInvalidException e) {
        return ResponseEntity.badRequest().body(makeListErrors("accessToken", "accessToken", e.getMessage()));
    }
}
