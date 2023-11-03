package stock.chart.domain;


import com.sun.istack.NotNull;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockFinance extends BaseTimeEntity implements Persistable<StockDateId> {

    @Id
    private StockDateId id;

    @MapsId("code")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_code")
    private Stock stock;

    @NotNull
    private int bps;

    @NotNull
    private int eps;


    @Override
    public boolean isNew() {
        return this.getCreatedDate() == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockFinance)) {
            return false;
        }
        StockFinance that = (StockFinance) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
