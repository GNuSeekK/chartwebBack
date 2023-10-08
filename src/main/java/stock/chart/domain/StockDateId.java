package stock.chart.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

@Embeddable
@Data
public class StockDateId implements Serializable {

    /**
     * TODO : 현재 hibernate 설정에 따라, 복합키가 자동으로 알파벳 순으로 생성되지만, 추후 DabaBase에서 DDL을 통해 생성할 때는 알파벳 순서가 아닌 (stock_code, original_date)로 변경해서 테스트 필요
     */
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "stock_code")
    private Stock stock;

    @Column(name = "original_date")
    private LocalDate date;

}
