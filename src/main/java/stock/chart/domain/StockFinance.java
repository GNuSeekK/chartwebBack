package stock.chart.domain;


import com.sun.istack.NotNull;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import stock.chart.domain.base.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockFinance extends BaseTimeEntity implements Persistable<StockDateId> {

    @Id
    private StockDateId id;

    @NotNull
    private int bps;

    @NotNull
    private int eps;


    @Override
    public boolean isNew() {
        return this.getCreatedDate() == null;
    }
}
