package stock.chart.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stock.chart.domain.base.BaseTimeEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;
    private String content;

    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "stock_code")
    private Stock stock;

}
