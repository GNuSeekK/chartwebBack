package stock.chart.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class SearchLogId implements Serializable {

    @Column(name = "search_date", updatable = false) // 개발 편의성을 위해 복합키 앞에 알파벳을 붙여야 하나, s -> u 이므로 복합키 구성에 문제가 없어서 생략
    private LocalDateTime date;

    @Column(name = "member_id")
    private String member;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchLogId)) {
            return false;
        }
        SearchLogId that = (SearchLogId) o;
        return Objects.equals(getDate(), that.getDate()) && Objects.equals(getMember(),
            that.getMember());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getMember());
    }

    public SearchLogId(String member) {
        this.member = member;
        this.date = LocalDateTime.now();
    }
}
