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

    private static final Duration EXPIRATION = Duration.ofSeconds(60);
    private static final String FLAG_KEY = "flag";
    private static final String PRIORITY_KEY = "priority";
    private static final String SAVING_KEY = "saving";

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
        redisTemplate.opsForValue().set(FLAG_KEY + code, flag);
        // make Value don't update
    }

    @Override
    public Optional<StockCashPriority> findByCode(String code) {

        RedisInteger priority = redisTemplate.opsForValue().get(PRIORITY_KEY + code);
        RedisInteger flag = redisTemplate.opsForValue().get(FLAG_KEY + code);

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
    public void saveFlag(String code) {
        redisTemplate.opsForValue().set(SAVING_KEY + code, new RedisInteger(0), EXPIRATION);
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
    public Optional<Integer> getSavingFlag(String code) {
        RedisInteger result = redisTemplate.opsForValue().get(SAVING_KEY + code);
        if (result != null) {
            return Optional.of(result.getValue());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getSaveFlag(String code) {
        RedisInteger result = redisTemplate.opsForValue().get(FLAG_KEY + code);
        if (result != null) {
            return Optional.of(result.getValue());
        }
        return Optional.empty();
    }
}
