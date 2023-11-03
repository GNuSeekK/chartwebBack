package stock.chart.stock.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import stock.chart.member.exception.PasswordNotMatchException;

import java.util.List;

@RestControllerAdvice
public class StockInfoControllerAdvice {

    public List<FieldError> makeListErrors(String objectName, String field, String message) {
        return List.of(
                new FieldError(objectName, field, message)
        );
    }

    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<List<FieldError>> StockNotFoundException(StockNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(makeListErrors("StockNotFound", "stock", e.getMessage()));
    }
}
