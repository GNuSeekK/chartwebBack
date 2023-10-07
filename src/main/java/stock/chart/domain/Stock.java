package stock.chart.domain;

import com.sun.istack.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import stock.chart.domain.base.BaseTimeEntity;


@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Setter
@Getter
public class Stock extends BaseTimeEntity implements Persistable<String>  {

    @Id
    @Column(name = "stock_code")
    private String code;

    @NotNull
    private String name;

    @OneToOne(mappedBy = "stock", fetch = javax.persistence.FetchType.LAZY)
    private Board board;

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
    public void setBoard() {
        this.board = new Board();
        this.board.setStock(this);
    }

}
