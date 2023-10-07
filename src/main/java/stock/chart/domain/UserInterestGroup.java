package stock.chart.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import stock.chart.domain.base.BaseTimeEntity;

@Entity
@Getter
@Setter
public class UserInterestGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "userInterestGroup")
    private List<UserInterestStock> userInterestStocks;

    /**
     * 양방향 연관관계 편의 메서드
     */
    public void addUserInterestStock(UserInterestStock userInterestStock) {
        this.userInterestStocks.add(userInterestStock);
        userInterestStock.setUserInterestGroup(this);
    }

}
