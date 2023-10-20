package stock.chart.login.exception;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import stock.chart.login.exception.KaKaoLoginFailException;
@RestControllerAdvice
public class LoginControllerAdvice {
    @ExceptionHandler(MemberNotMatchException.class)
    public ResponseEntity<List<FieldError>> memberNotMatchException(MemberNotMatchException e) {
        return ResponseEntity.badRequest().body(makeListErrors("loginMemberForm", "member", e.getMessage()));
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ResponseEntity<List<FieldError>> refreshTokenInvalidException(RefreshTokenInvalidException e) {
        return ResponseEntity.badRequest().body(makeListErrors("refreshToken", "refreshToken", e.getMessage()));
    }

    @ExceptionHandler(UsedTokenException.class)
    public ResponseEntity<List<FieldError>> usedTokenError(UsedTokenException e) {
        return ResponseEntity.badRequest().body(makeListErrors("refreshToken", "usedRefreshToken", e.getMessage()));
    }

    public List<FieldError> makeListErrors(String objectName, String field, String message) {
        return List.of(
            new FieldError(objectName, field, message)
        );
    }


    /**
     * 카카오 로그인 실패
     */
    @ExceptionHandler(KaKaoLoginFailException.class)
    public ResponseEntity<List<FieldError>> kaKaoLoginFailException(KaKaoLoginFailException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(makeListErrors("kakaoLoginFail", "kakao", e.getMessage()));
    }

    /**
     * 회원가입 필요
     * rejectedValue : 회원가입에 실패한 이메일이 반환되어 나감 => 추후 프론트에서 회원가입 창에 사용
     */
    @ExceptionHandler(NeedRegisterMemberException.class)
    public ResponseEntity<List<FieldError>> needRegisterMemberException(NeedRegisterMemberException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            List.of(
                new FieldError("needRegisterMember", "email", e.getMessage(), false, null, null, "회원가입이 필요합니다.")
            )
        );
    }
}
