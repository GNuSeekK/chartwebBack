package stock.chart.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import stock.chart.domain.Stock;
import stock.chart.domain.StockDateId;
import stock.chart.domain.StockPrice;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;



@AllArgsConstructor
@Getter
public class StockPriceDto {
    private LocalDate date;
    private int open;
    private int high;
    private int low;
    private int close;
    private int volume;

}
