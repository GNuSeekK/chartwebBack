package stock.chart.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stock.chart.domain.base.BaseTimeEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInterestStock extends BaseTimeEntity {

    @Id
    @Column(name = "member_interest_stock_id")
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private MemberInterestGroup memberInterestGroup;

    public void setMemberInterestGroup(MemberInterestGroup memberInterestGroup) {
        this.memberInterestGroup = memberInterestGroup;
    }
}
