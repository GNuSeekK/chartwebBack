package stock.chart.stock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stock.chart.domain.Stock;
import stock.chart.stock.dto.StockDataDto;
import stock.chart.stock.repository.StockRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private final StockRepository stockRepository;

    public StockDataDto getStockInfo(String code) {
        return stockRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."));
    }
}
