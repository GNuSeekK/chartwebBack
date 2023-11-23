package stock.chart.stock.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CacheStockPrice {

    private Integer open;
    private Integer high;
    private Integer low;
    private Integer close;
    private Integer volume;
    private Long date;

}
