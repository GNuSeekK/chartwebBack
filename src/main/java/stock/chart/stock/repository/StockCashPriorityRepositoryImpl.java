package stock.chart.stock.repository;

import java.time.Duration;
import java.util.Optional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import stock.chart.domain.redis.RedisInteger;
import stock.chart.domain.redis.StockCashPriority;

@Repository
public class StockCashPriorityRepositoryImpl implements
    StockCashPriorityRepository {

    private final RedisTemplate<String, RedisInteger> redisTemplate;
//    private final RedisTemplate<String, RedisInteger> flagTemplate;

    private static final Duration EXPIRATION = Duration.ofSeconds(5);
    private static final String SAVED_FLAG_KEY = "flag";
    private static final String PRIORITY_KEY = "priority";
    private static final String LOCK_KEY = "lock";

    public StockCashPriorityRepositoryImpl(RedisTemplate<String, RedisInteger> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Async
    @Override
    public void updatePriorityAndExpiration(String code) {

        RedisInteger priority = redisTemplate.opsForValue().get(PRIORITY_KEY + code);
        if (priority != null) {
            priority.decrease();
            redisTemplate.opsForValue().set(PRIORITY_KEY + code, priority, EXPIRATION);
        }
    }

    @Override
    public void updateSaveFlag(String code, int saveFlag) {
        RedisInteger flag = new RedisInteger(saveFlag);
        redisTemplate.opsForValue().set(SAVED_FLAG_KEY + code, flag);
        // make Value don't update
    }

    @Override
    public Optional<StockCashPriority> findByCode(String code) {

        RedisInteger priority = redisTemplate.opsForValue().get(PRIORITY_KEY + code);
        RedisInteger flag = redisTemplate.opsForValue().get(SAVED_FLAG_KEY + code);

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
    public void saveLockFlag(String code) {
        redisTemplate.opsForValue().set(LOCK_KEY + code, new RedisInteger(0), EXPIRATION);
    }

    @Override
    public void save(StockCashPriority stockCashPriority) {
        redisTemplate.opsForValue()
            .set(PRIORITY_KEY + stockCashPriority.getCode(), new RedisInteger(stockCashPriority.getPriority()));
        redisTemplate.expire(PRIORITY_KEY + stockCashPriority.getCode(), EXPIRATION);
    }

    @Override
    public Optional<Integer> getPriority(String code) {
        RedisInteger result = redisTemplate.opsForValue().get(PRIORITY_KEY + code);
        if (result != null) {
            return Optional.of(result.getValue());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getLockFlag(String code) {
        RedisInteger result = redisTemplate.opsForValue().get(LOCK_KEY + code);
        if (result != null) {
            return Optional.of(result.getValue());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getSavedFlag(String code) {
        RedisInteger result = redisTemplate.opsForValue().get(SAVED_FLAG_KEY + code);
        if (result != null) {
            return Optional.of(result.getValue());
        }
        return Optional.empty();
    }

    @Override
    public void invalidateSaveFlag(String code) {
        redisTemplate.delete(SAVED_FLAG_KEY + code);
    }
}
