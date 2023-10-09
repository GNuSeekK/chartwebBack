package stock.chart.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

@Data
@Embeddable
public class SearchLogId implements Serializable {

    @Column(name = "search_date") // 개발 편의성을 위해 복합키 앞에 알파벳을 붙여야 하나, s -> u 이므로 복합키 구성에 문제가 없어서 생략
    private LocalDate date;

    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
