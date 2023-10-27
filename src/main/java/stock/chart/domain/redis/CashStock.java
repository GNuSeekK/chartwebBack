package stock.chart.domain.redis;

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
public class CashStock {

    @Id
    @Indexed
    private String code;
    private String name;

    private List<CashStockPrice> cashStockPriceList = new ArrayList<>();

    public List<StockPriceDto> getStockPrices() {
        return cashStockPriceList.stream()
            .map(CashStockPrice::toStockPriceDto)
            .collect(Collectors.toList());
    }

}
