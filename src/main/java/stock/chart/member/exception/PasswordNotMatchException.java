package stock.chart.member.exception;

import org.springframework.validation.FieldError;

public class PasswordNotMatchException extends RuntimeException {

        public PasswordNotMatchException() {
            super("비밀번호가 일치하지 않습니다.");
        }

        public FieldError getFieldError(String objectName) {
            return new FieldError(objectName, "password", "비밀번호가 일치하지 않습니다.");
        }

}
