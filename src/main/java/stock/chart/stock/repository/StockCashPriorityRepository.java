package stock.chart.stock.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import stock.chart.domain.redis.StockCashPriority;

public interface StockCashPriorityRepository extends JpaRepository<StockCashPriority, String> {
    Optional<StockCashPriority> findByCode(String code);
}
