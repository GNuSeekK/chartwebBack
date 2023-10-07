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

    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "a_stock_code") // 개발 편의성을 위해, 복합키 Primary Key 생성 규칙에 따라 알파벳 순서가 아닌 a_를 붙임, 추후 DabaBase에서 DDL을 통해 생성할 때는 a_를 제거해야 함
    private Stock stock;

    @Column(name = "original_date")
    private LocalDate date;

}
