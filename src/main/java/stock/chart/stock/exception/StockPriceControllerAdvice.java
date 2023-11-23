package stock.chart.stock.exception;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class StockPriceControllerAdvice {

    public List<FieldError> makeListErrors(String objectName, String field, String message) {
        return List.of(
            new FieldError(objectName, field, message)
        );
    }

    @ExceptionHandler(IllegalStockException.class)
    public ResponseEntity<List<FieldError>> illegalStockException(IllegalStockException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(makeListErrors("stock", "stock", e.getMessage()));
    }


}
