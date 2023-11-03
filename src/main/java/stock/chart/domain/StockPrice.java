package stock.chart.domain;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import stock.chart.domain.base.BaseTimeEntity;

import java.util.Objects;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockPrice extends BaseTimeEntity implements Persistable<StockDateId> {

    @EmbeddedId
    private StockDateId id;


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



    public void setStock(Stock stock) {
        this.stock = stock;
    }

}
