package stock.chart.stock.jmetertest.repository;

import java.util.Optional;
import stock.chart.stock.jmetertest.entity.TestCashStock;

public interface TestCashStockCustomRepository {

    Optional<TestCashStock> getCashStockValue(String code);

    void setCashStockValue(String code, TestCashStock testCashStock);
}
