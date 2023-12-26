package stock.chart.stock.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stock.chart.stock.dto.SearchLogSumDto;
import stock.chart.stock.repository.SearchLogRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SearchLogService {

    private final SearchLogRepository searchLogRepository;

    public List<SearchLogSumDto> getTodayTop3() {
        Pageable pageable = PageRequest.of(0, 3);
        List<SearchLogSumDto> searchLogDtos = searchLogRepository.findTodayTop3ByOrderBySumDesc(pageable)
            .orElseGet(List::of);
        log.info("size: {}", searchLogDtos.size());
        List<String> dummyCodes = List.of("005930", "035420", "035720");
        if (searchLogDtos.size() < 3) {
            addDummyLogs(searchLogDtos, dummyCodes);
        }
        return searchLogDtos;
    }

    private static void addDummyLogs(List<SearchLogSumDto> searchLogDtos, List<String> dummyCodes) {
        int size = 3 - searchLogDtos.size();
        for (int i = 0; i < size; i++) {
            searchLogDtos.add(new SearchLogSumDto(0L, dummyCodes.get(i)));
        }
    }
}
