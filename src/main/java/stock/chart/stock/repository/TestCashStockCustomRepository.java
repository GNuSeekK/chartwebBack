package stock.chart.stock.repository;

import java.util.Optional;
import stock.chart.domain.redis.TestCashStock;

public interface TestCashStockCustomRepository {

    Optional<TestCashStock> getCashStockValue(String code);

    void setCashStockValue(String code, TestCashStock testCashStock);
}
