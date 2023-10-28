package stock.chart.stock.repository;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import stock.chart.domain.redis.RedisInteger;
import stock.chart.domain.redis.StockCashPriority;

@Repository
public class StockCashPriorityRepositoryImpl implements
    StockCashPriorityRepository {

    private final RedisTemplate<String, RedisInteger> priorityTemplate;
    private final RedisTemplate<String, RedisInteger> flagTemplate;

    private static final Duration EXPIRATION = Duration.ofSeconds(10);
    private static final String FLAG_KEY = "flag";
    private static final String PRIORITY_KEY = "priority";

    public StockCashPriorityRepositoryImpl(RedisTemplate<String, RedisInteger> priorityTemplate,
        RedisTemplate<String, RedisInteger> flagTemplate) {
        this.priorityTemplate = priorityTemplate;
        this.flagTemplate = flagTemplate;
    }

    @Async
    @Override
    public void updatePriorityAndExpiration(String code) {

        RedisInteger priority = priorityTemplate.opsForValue().get(PRIORITY_KEY + code);
        if (priority != null) {
            priority.decrease();
            priorityTemplate.opsForValue().set(PRIORITY_KEY + code, priority, EXPIRATION);
        }
    }

    @Override
    public void updateSaveFlag(String code, int saveFlag) {
        RedisInteger flag = new RedisInteger(saveFlag);
        flagTemplate.opsForValue().set(FLAG_KEY + code, flag);
    }

    @Override
    public Optional<StockCashPriority> findByCode(String code) {

        RedisInteger priority = priorityTemplate.opsForValue().get(PRIORITY_KEY + code);
        RedisInteger flag = flagTemplate.opsForValue().get(FLAG_KEY + code);

        if (priority != null && flag != null) {
            return Optional.of(StockCashPriority.builder()
                .code(code)
                .priority(priority.getValue())
                .saveFlag(flag.getValue())
                .build());
        }
        return Optional.empty();
    }

    @Override
    public void save(StockCashPriority stockCashPriority) {
        priorityTemplate.opsForValue().set(PRIORITY_KEY + stockCashPriority.getCode() , new RedisInteger(stockCashPriority.getPriority()));
        priorityTemplate.expire(PRIORITY_KEY + stockCashPriority.getCode(), EXPIRATION);
        flagTemplate.opsForValue().set(FLAG_KEY + stockCashPriority.getCode(), new RedisInteger(stockCashPriority.getSaveFlag()));
    }

    @Override
    public Optional<Integer> getPriority(String code) {
        RedisInteger result = priorityTemplate.opsForValue().get(PRIORITY_KEY + code);
        if (result != null) {
            return Optional.of(result.getValue());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getSaveFlag(String code) {
        RedisInteger result = flagTemplate.opsForValue().get(FLAG_KEY + code);
        if (result != null) {
            return Optional.of(result.getValue());
        }
        return Optional.empty();
    }
}
