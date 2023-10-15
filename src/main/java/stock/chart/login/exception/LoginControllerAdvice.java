package stock.chart.login.exception;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import stock.chart.login.dto.UsedTokenError;

@RestControllerAdvice
public class LoginControllerAdvice {
    @ExceptionHandler(MemberNotMatchException.class)
    public ResponseEntity memberNotMatchException(MemberNotMatchException e) {
        return ResponseEntity.badRequest().body(makeListErrors("loginMemberForm", "member", e.getMessage()));
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ResponseEntity refreshTokenInvalidException(RefreshTokenInvalidException e) {
        return ResponseEntity.badRequest().body(makeListErrors("refreshToken", "refreshToken", e.getMessage()));
    }

    @ExceptionHandler(UsedTokenException.class)
    public ResponseEntity usedTokenError(UsedTokenException e) {
        return ResponseEntity.badRequest().body(makeListErrors("refreshToken", "usedRefreshToken", e.getMessage()));
    }

    public List<FieldError> makeListErrors(String objectName, String field, String message) {
        return List.of(
            new FieldError(objectName, field, message)
        );
    }

}
