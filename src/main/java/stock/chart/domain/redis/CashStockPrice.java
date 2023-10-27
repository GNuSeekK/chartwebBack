package stock.chart.domain.redis;


import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import stock.chart.stock.dto.StockPriceDto;

@RedisHash("cash_stock_price")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashStockPrice implements Comparable<CashStockPrice>, Serializable {



    @Id
    private String code;
    private LocalDate originalDate;
    private int open;
    private int high;
    private int low;
    private int close;
    private Long volume;

    public StockPriceDto toStockPriceDto() {
        return StockPriceDto.builder()
            .code(code)
            .date(originalDate)
            .open(open)
            .high(high)
            .low(low)
            .close(close)
            .volume(volume)
            .build();
    }

    @Override
    public int compareTo(CashStockPrice o) {
        return this.originalDate.compareTo(o.originalDate);
    }
}
