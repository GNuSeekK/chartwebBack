package stock.chart.member.exception;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class DuplicateMemberException extends RuntimeException {

    private String field;
    public DuplicateMemberException(String message, String field) {
        super(message);
        this.field = field;
    }

    public FieldError getFieldError() {
        return new FieldError("signUpForm", field, getMessage());
    }


}
