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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Builder
public class Stock extends BaseTimeEntity implements Persistable<String> {

    @Id
    @Column(name = "stock_code")
    private String code;
    @NotNull
    private String name;


    @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY)
    private List<Board> board = new ArrayList<>();

    @Override
    public String getId() {
        return this.getCode();
    }

    @Override
    public boolean isNew() {
        return this.getCreatedDate() == null;
    }

    /**
     * 양방향 연관관계 어시스트 메서드
     */
    public void setBoard(Board board) {
        this.board.add(board);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stock)) {
            return false;
        }
        Stock stock = (Stock) o;
        return Objects.equals(getCode(), stock.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }
}
