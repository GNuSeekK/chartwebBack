package stock.chart.stock.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import stock.chart.domain.SearchLog;
import stock.chart.stock.dto.SearchLogSumDto;

public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {

    @Query("SELECT new stock.chart.stock.dto.SearchLogSumDto(COUNT(sl), sl.stock.code) "
        + "FROM SearchLog sl "
        + "WHERE sl.id.date >= CURRENT_DATE "
        + "GROUP BY sl.stock.code "
        + "ORDER BY COUNT(sl) DESC")
    Optional<List<SearchLogSumDto>> findTodayTop3ByOrderBySumDesc(Pageable pageable);
}
