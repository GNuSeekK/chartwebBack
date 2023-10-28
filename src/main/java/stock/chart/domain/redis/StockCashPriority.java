package stock.chart.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockCashPriority {

    private String code;
    private int priority;
    private int saveFlag = 0;

}
