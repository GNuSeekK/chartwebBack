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
public class MemberInterestGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "memberInterestGroup")
    private List<MemberInterestStock> memberInterestStocks;

    /**
     * 양방향 연관관계 편의 메서드
     */
    public void addUserInterestStock(MemberInterestStock memberInterestStock) {
        this.memberInterestStocks.add(memberInterestStock);
        memberInterestStock.setMemberInterestGroup(this);
    }

}
