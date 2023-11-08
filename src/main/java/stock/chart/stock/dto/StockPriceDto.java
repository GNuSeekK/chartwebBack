package stock.chart.stock.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StockPriceDto {

    private LocalDate date;
    private int open;
    private int high;
    private int low;
    private int close;
    private int volume;

}
