package stock.chart.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class SearchLogId implements Serializable {

    @CreatedDate
    @Column(name = "search_date") // 개발 편의성을 위해 복합키 앞에 알파벳을 붙여야 하나, s -> u 이므로 복합키 구성에 문제가 없어서 생략
    private LocalDate date;

    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
