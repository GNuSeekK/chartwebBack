package stock.chart.stock.repository;

import java.util.Optional;
import stock.chart.domain.redis.StockCashPriority;

public interface StockCashPriorityRepository {

    void updatePriorityAndExpiration(String code);

    void updateSaveFlag(String code, int saveFlag);

    Optional<StockCashPriority> findByCode(String code);

    void save(StockCashPriority stockCashPriority);

    Optional<Integer> getPriority(String code);

    Optional<Integer> getSaveFlag(String code);

    void saveFlag(String code);

    Optional<Integer> getSavingFlag(String code);

    void invalidateSaveFlag(String code);
}
