package stock.chart.domain;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockPrice extends BaseTimeEntity implements Persistable<StockDateId> {

    @Id
    private StockDateId id;


    private int open;
    private int high;
    private int low;
    private int close;
    private Long volume;


    @Override
    public boolean isNew() {
        return this.getCreatedDate() == null;
    }
}
