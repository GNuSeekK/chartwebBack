package stock.chart.stock.repository;

import java.util.Optional;
import stock.chart.domain.redis.StockCashPriority;

public interface StockCashPriorityRepository {

    void updatePriorityAndExpiration(String code);

    void updateSaveFlag(String code, int saveFlag);

    Optional<StockCashPriority> findByCode(String code);

    void save(StockCashPriority stockCashPriority);

    Optional<Integer> getPriority(String code);

    Optional<Integer> getSavedFlag(String code);

    void saveLockFlag(String code);

    Optional<Integer> getLockFlag(String code);

    void invalidateSaveFlag(String code);
}
