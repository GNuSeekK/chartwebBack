package stock.chart.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stock.chart.domain.StockPrice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {
    @Query("select sp from StockPrice sp where sp.id.code = :code and sp.id.date between :start and :end")
    Optional<List<StockPrice>> findAllWithDate(@Param("code") String code, @Param("start") LocalDate start, @Param("end") LocalDate end);

}
