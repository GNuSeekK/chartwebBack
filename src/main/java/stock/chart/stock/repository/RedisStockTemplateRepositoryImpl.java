package stock.chart.stock.repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import stock.chart.domain.redis.CashStock;
import stock.chart.domain.redis.CashStockPrice;
import stock.chart.domain.redis.StockCashPriority;


@Slf4j
public class RedisStockTemplateRepositoryImpl implements
    RedisStockTemplateRepository {

    private final RedisTemplate<String, CashStockPrice> redisPriceTemplate;
    private final ZSetOperations<String, CashStockPrice> zPriceSetOperations;
    private final RedisTemplate<String, CashStock> redisTemplate;
    private final StockCashPriorityRepository stockCashPriorityRepository;


    public RedisStockTemplateRepositoryImpl(RedisTemplate<String, CashStockPrice> redisPriceTemplate, RedisTemplate<String, CashStock> redisTemplate, StockCashPriorityRepository stockCashPriorityRepository) {
        this.redisPriceTemplate = redisPriceTemplate;
        this.zPriceSetOperations = redisPriceTemplate.opsForZSet();
        this.redisTemplate = redisTemplate;
        this.stockCashPriorityRepository = stockCashPriorityRepository;
    }

    @Async
    @Override
    public void saveSortedSet(String key, Set<CashStockPrice> cashStockPricesSet) {
        cashStockPricesSet.parallelStream()
            .forEach(cashStockPrice -> zPriceSetOperations.add(key, cashStockPrice, cashStockPrice.getOriginalDate().toEpochDay()));
        log.info("저장이 성공적으로 완료 되었습니다");
        stockCashPriorityRepository.save(StockCashPriority.builder()
            .code(key)
            .priority(0)
            .saveFlag(1)
            .build());
    }

    @Override
    public Optional<CashStock> getCashStockWithSortedStockPrice(String key, LocalDate startDate, LocalDate endDate) {
        Set<CashStockPrice> cashStockPricesSet = zPriceSetOperations.rangeByScore(key, startDate.toEpochDay(), endDate.toEpochDay());
        if (cashStockPricesSet == null || cashStockPricesSet.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(CashStock.builder()
            .code(key)
            .cashStockPricesSet(cashStockPricesSet)
            .build());
    }
}
