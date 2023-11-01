package stock.chart.stock.jmetertest.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import stock.chart.stock.jmetertest.entity.TestCashStock;

public interface TestCashStockRepository extends JpaRepository<TestCashStock, String>, TestCashStockCustomRepository {

    Optional<TestCashStock> findByCode(String code);

}
