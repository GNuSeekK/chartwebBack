package stock.chart.stock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stock.chart.domain.Stock;
import stock.chart.domain.StockPrice;
import stock.chart.stock.dto.StockDataDto;
import stock.chart.stock.repository.StockPriceRepository;
import stock.chart.stock.repository.StockRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private final StockRepository stockRepository;
    private final StockPriceRepository stockPriceRepository;

    public StockDataDto getStockName(String code) {
        return stockRepository.findStockNameById(code)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."));
    }

    public Stock getStock(String code){
        return stockRepository.findStockById(code)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."));
    }

    public List<StockPrice> getStockPrice(String code, LocalDate start, LocalDate end) {
        return stockPriceRepository.findAll(code, start, end)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."));
    }
}
