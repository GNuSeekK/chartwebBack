package stock.chart.domain.redis;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import stock.chart.stock.dto.StockPriceDto;

@RedisHash("cash_stock")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestCashStock implements Serializable {

    @Id
    @Indexed
    private String code;

    private List<CashStockPrice> cashStockPricesSet = new ArrayList<>();

    public List<StockPriceDto> getStockPrices() {
        return cashStockPricesSet.parallelStream()
            .map(cashStockPrice -> cashStockPrice.toStockPriceDto(code))
            .collect(Collectors.toList());
    }


}
