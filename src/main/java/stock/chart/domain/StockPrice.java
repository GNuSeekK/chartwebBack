package stock.chart.domain;

import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;
import stock.chart.domain.base.BaseTimeEntity;
import stock.chart.domain.redis.CashStockPrice;
import stock.chart.stock.dto.StockPriceDto;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockPrice extends BaseTimeEntity implements Persistable<StockDateId>{

    @EmbeddedId
    private StockDateId id;

    public StockPrice(StockDateId id, int open, int high, int low, int close, int volume) {
        this.id = id;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    @MapsId("code")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_code")
    private Stock stock;

    private int open;
    private int high;
    private int low;
    private int close;
    private int volume;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockPrice)) {
            return false;
        }
        StockPrice that = (StockPrice) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean isNew() {
        return this.getCreatedDate() == null;
    }


    public CashStockPrice toCashStockPrice() {
        return CashStockPrice.builder()
            .originalDate(this.id.getDate())
            .open(this.open)
            .high(this.high)
            .low(this.low)
            .close(this.close)
            .volume(this.volume)
            .build();
    }

    public StockPriceDto toStockPriceDto() {
        return StockPriceDto.builder()
            .code(this.id.getCode())
            .date(this.id.getDate())
            .open(this.open)
            .high(this.high)
            .low(this.low)
            .close(this.close)
            .volume(this.volume)
            .build();
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

}
