package stock.chart.stock.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import stock.chart.domain.redis.CashStock;


public interface RedisStockRepository extends JpaRepository<CashStock, String>, RedisStockTemplateRepository {

//    Optional<CashStock> findByCode(String code);

}
