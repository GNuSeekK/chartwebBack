package stock.chart.stock.dto;


import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockPriceListDto {

    private String stockCode;
    private List<StockPriceDto> stockPriceDtoList;

}
