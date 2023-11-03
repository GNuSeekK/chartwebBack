package stock.chart.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stock.chart.domain.StockDateId;
import stock.chart.domain.StockPrice;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {
    //기간별을 잡고 가져오는 것
    @Query("select sp from StockPrice sp where sp.id.code = :code and sp.id.date between :start and :end")
    Optional<List<StockPrice>> findAll(@Param("code") String code, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("select sp from StockPrice sp where sp.id.code = :code")
    Optional<List<StockPrice>> findByCode(@Param("code") String code);

    @Query("select sp from StockPrice sp")
    Optional<List<StockPrice>> findAllForDummy();

}
