package stock.chart.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class StockPriceParam {

    @NotNull(message = "주식코드 null")
    private String code;

    @DateTimeFormat(pattern="yyyyMMdd")
    private LocalDate start;

    @DateTimeFormat(pattern="yyyyMMdd")
    private LocalDate end;


}
