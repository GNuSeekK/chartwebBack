package stock.chart.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
@RedisHash("stock_cash_priority")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockCashPriority {

    @Id
    @Indexed
    private String code;
    private int priority;
    private int saveFlag = 0;

    @TimeToLive
    private Long expiration;

    public void updatePriorityAndExpiration(Long expiration) {
        this.priority--;
        this.expiration = expiration;
    }

    public void setSaveFlag(int saveFlag) {
        this.saveFlag = saveFlag;
    }
}
