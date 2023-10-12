package stock.chart.member.exception;

import org.springframework.validation.FieldError;

public class DuplicateMemberException extends RuntimeException {

    private String field;
    public DuplicateMemberException(String message, String field) {
        super(message);
        this.field = field;
    }

    public FieldError getFieldError(String objectName) {
        return new FieldError(objectName, field, getMessage());
    }


}
