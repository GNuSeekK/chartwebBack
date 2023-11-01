package stock.chart.stock.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import stock.chart.domain.redis.CashStock;
import stock.chart.domain.redis.CashStockPrice;


@Slf4j
public class RedisStockTemplateRepositoryImpl implements
    RedisStockTemplateRepository {

    private final RedisTemplate<String, CashStockPrice> redisPriceTemplate;
    private final ZSetOperations<String, CashStockPrice> zPriceSetOperations;
    private final StockCashPriorityRepository stockCashPriorityRepository;


    public RedisStockTemplateRepositoryImpl(RedisTemplate<String, CashStockPrice> redisPriceTemplate, StockCashPriorityRepository stockCashPriorityRepository) {
        this.redisPriceTemplate = redisPriceTemplate;
        this.zPriceSetOperations = redisPriceTemplate.opsForZSet();
        this.stockCashPriorityRepository = stockCashPriorityRepository;
    }

    @Override
    public void saveSortedSet(String key, Set<CashStockPrice> cashStockPricesSet) {
        stockCashPriorityRepository.saveLockFlag(key);
        cashStockPricesSet.parallelStream()
            .forEach(cashStockPrice -> zPriceSetOperations.add(key, cashStockPrice, cashStockPrice.getOriginalDate().toEpochDay()));
        log.info("저장이 성공적으로 완료 되었습니다");
        stockCashPriorityRepository.updateSaveFlag(key, 1);
    }

    @Override
    public Optional<CashStock> getCashStockWithSortedStockPrice(String key, LocalDate startDate, LocalDate endDate) {
        long date = System.currentTimeMillis();
        Set<CashStockPrice> cashStockPricesSet = zPriceSetOperations.rangeByScore(key, startDate.toEpochDay(), endDate.toEpochDay());
        log.info("redis 조회 시간 : {}", System.currentTimeMillis() - date);
        if (cashStockPricesSet == null || cashStockPricesSet.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(CashStock.builder()
            .code(key)
            .cashStockPricesSet(cashStockPricesSet)
            .build());
    }

    @Override
    public Optional<CashStock> findByCode(String code) {
        Date date = new Date();
        Set<CashStockPrice> cashStockPricesSet = zPriceSetOperations.range(code, 0, -1);
        log.info("redis 조회 시간 : {}", new Date().getTime() - date.getTime());
        if (cashStockPricesSet == null || cashStockPricesSet.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(CashStock.builder()
            .code(code)
            .cashStockPricesSet(cashStockPricesSet)
            .build());
    }
}
