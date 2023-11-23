package stock.chart.stock.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Builder
public class StockPriceRequestForm {

    @NotEmpty(message = "종목 코드를 입력해주세요.")
    private String code;
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate start;
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate end;

    public void inputDefaultDate() {
        if (start == null) {
            start = LocalDate.now().minusYears(1);
        }
        if (end == null) {
            end = LocalDate.now();
        }
    }

}
