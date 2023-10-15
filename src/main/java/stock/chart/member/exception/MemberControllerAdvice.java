package stock.chart.member.exception;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberControllerAdvice {

    public List<FieldError> makeListErrors(String objectName, String field, String message) {
        return List.of(
            new FieldError(objectName, field, message)
        );
    }

    @ExceptionHandler(InvalidMemberException.class)
    public ResponseEntity invalidMemberException(InvalidMemberException e) {
        return ResponseEntity.badRequest().body(makeListErrors("memberInfoChangeForm", "member", e.getMessage()));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity duplicateEmailException(DuplicateEmailException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(makeListErrors("signUpForm", "email", e.getMessage()));
    }

    @ExceptionHandler(DuplicateNicknameException.class)
    public ResponseEntity duplicateNicknameException(DuplicateNicknameException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(makeListErrors("signUpForm", "nickname", e.getMessage()));
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity passwordNotMatchException(PasswordNotMatchException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(makeListErrors("passwordChangeForm", "password", e.getMessage()));
    }

    @ExceptionHandler(PasswordConfirmException.class)
    public ResponseEntity passwordConfirmException(PasswordConfirmException e) {
        return ResponseEntity.badRequest().body(makeListErrors("passwordChangeForm", "newPasswordConfirm", e.getMessage()));
    }

}
