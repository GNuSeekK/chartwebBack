package stock.chart.stock.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import stock.chart.domain.Stock;

@Getter
public class StockListDto {

    private final List<StockNameDto> stockList;

    public StockListDto(List<Stock> stockList) {
        this.stockList = stockList.stream()
            .map(StockNameDto::new)
            .collect(Collectors.toList());
    }
}
