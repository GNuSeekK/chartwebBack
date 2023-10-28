package stock.chart.stock.repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import stock.chart.domain.redis.CashStock;
import stock.chart.domain.redis.CashStockPrice;

public interface RedisStockTemplateRepository {

    void saveSortedSet(String key, Set<CashStockPrice> cashStockPricesSet);
    Optional<CashStock> getCashStockWithSortedStockPrice(String key, LocalDate startDate, LocalDate endDate);

    Optional<CashStock> findByCode(String code);

}
