package stock.chart.stock.repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;
import stock.chart.stock.entity.CacheStockPrice;

@Slf4j
@Repository
public class CacheStockPriceRepository {

    private final ZSetOperations<String, CacheStockPrice> zSetOperations;

    public CacheStockPriceRepository(RedisTemplate<String, CacheStockPrice> redisTemplate) {
        this.zSetOperations = redisTemplate.opsForZSet();
    }

    public Optional<Set<CacheStockPrice>> getDataWithDate(String code, LocalDate start, LocalDate end) {
        return Optional.ofNullable(zSetOperations.rangeByScore(code, start.toEpochDay(), end.toEpochDay()));
    }

}
