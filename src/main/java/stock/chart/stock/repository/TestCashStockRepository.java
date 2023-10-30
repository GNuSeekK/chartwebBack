package stock.chart.stock.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import stock.chart.domain.redis.TestCashStock;

public interface TestCashStockRepository extends JpaRepository<TestCashStock, String>, TestCashStockCustomRepository {

    Optional<TestCashStock> findByCode(String code);

}
