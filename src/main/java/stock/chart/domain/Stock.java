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
import java.util.List;


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

    @OneToMany(mappedBy = "stock", fetch = javax.persistence.FetchType.LAZY)
    private List<Board> board;

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

}
