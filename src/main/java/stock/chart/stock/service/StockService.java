package stock.chart.stock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stock.chart.domain.Stock;
import stock.chart.domain.StockPrice;
import stock.chart.stock.dto.StockDataDto;
import stock.chart.stock.dto.StockPriceDto;
import stock.chart.stock.dto.StockPriceParam;
import stock.chart.stock.exception.StockNotFoundException;
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
                .orElseThrow(StockNotFoundException::new);
    }

    public Stock getStock(String code){
        return stockRepository.findStockById(code)
                .orElseThrow(StockNotFoundException::new);
    }

    public List<StockPriceDto> getStockPrice(StockPriceParam stockPriceParam) {
        String code = stockPriceParam.getCode();
        LocalDate start = stockPriceParam.getStart();
        LocalDate end = stockPriceParam.getEnd();

        //주식코드 검증
        getStock(code);

        if(start == null || end == null){ //데이터가 없으면 최근 1년 정보
            end = LocalDate.now();
            start = end.minusYears(1);
        }
        return stockPriceRepository.findAll(code, start, end)
                .orElseThrow(StockNotFoundException::new);
    }
}
