package stock.chart.stock.dto;

import java.time.LocalDate;
import lombok.Getter;
import stock.chart.domain.StockPrice;
import stock.chart.stock.entity.CacheStockPrice;

@Getter
public class StockPriceDto {

    private final Integer open;
    private final Integer high;
    private final Integer low;
    private final Integer close;
    private final Integer volume;
    private final LocalDate date;


    public StockPriceDto(StockPrice stockPrice) {
        this.open = stockPrice.getOpen();
        this.high = stockPrice.getHigh();
        this.low = stockPrice.getLow();
        this.close = stockPrice.getClose();
        this.volume = stockPrice.getVolume();
        this.date = stockPrice.getDate();
    }

    public StockPriceDto(CacheStockPrice cacheStockPrice) {
        this.open = cacheStockPrice.getOpen();
        this.high = cacheStockPrice.getHigh();
        this.low = cacheStockPrice.getLow();
        this.close = cacheStockPrice.getClose();
        this.volume = cacheStockPrice.getVolume();
        this.date = LocalDate.ofEpochDay(cacheStockPrice.getDate());
    }
}
