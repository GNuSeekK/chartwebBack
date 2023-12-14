package stock.chart.stock.dto;

import lombok.Getter;
import stock.chart.domain.Stock;

@Getter
public class StockNameDto {

    private final String code;
    private final String name;

    public StockNameDto(Stock stock) {
        this.code = stock.getCode();
        this.name = stock.getName();
    }


}
