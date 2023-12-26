package stock.chart.stock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stock.chart.stock.dto.StockListDto;
import stock.chart.stock.repository.StockRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public StockListDto getStockList() {
        return new StockListDto(stockRepository.findAllStockNameInfo().orElseThrow());
    }

}
