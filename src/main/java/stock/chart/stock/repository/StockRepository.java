package stock.chart.stock.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import stock.chart.domain.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("select s from Stock s")
    Optional<List<Stock>> findAllStockNameInfo();

}
