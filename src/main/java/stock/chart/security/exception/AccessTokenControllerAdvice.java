package stock.chart.security.exception;


import java.util.List;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<FieldError>> accessTokenInvalidException(AccessTokenInvalidException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(makeListErrors("accessToken", "accessToken", e.getMessage()));
    }
}
