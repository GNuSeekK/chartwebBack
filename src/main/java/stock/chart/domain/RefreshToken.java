package stock.chart.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;
import stock.chart.domain.base.BaseTimeEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RefreshToken extends BaseTimeEntity implements Persistable<String> {

    @Id
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private RefreshTokenStatus status;

    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Override
    public String getId() {
        return this.getRefreshToken();
    }

    @Override
    public boolean isNew() {
        return this.getCreatedDate() == null;
    }


    public void updateStatus(RefreshTokenStatus refreshTokenStatus) {
        this.status = refreshTokenStatus;
    }

}
