package stock.chart.stock.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PopularStockPriceListDto {

    private final List<StockPriceListDto> popularStockList;

}
