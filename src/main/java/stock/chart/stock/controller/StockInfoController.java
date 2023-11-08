package stock.chart.stock.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stock.chart.domain.Stock;
import stock.chart.stock.dto.StockDataDto;
import stock.chart.stock.dto.StockPriceDto;
import stock.chart.stock.dto.StockPriceParam;
import stock.chart.stock.service.StockService;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
public class StockInfoController {
    private final StockService stockService;

    @GetMapping("/search")
    public ResponseEntity<StockDataDto> getStockName(String code) {
        return ResponseEntity.ok(stockService.getStockName(code));
    }

    @GetMapping("/info")
    public ResponseEntity<Stock> getStock(String code) {
        return ResponseEntity.ok(stockService.getStock(code));
    }


    @GetMapping("/price")
    public ResponseEntity<List<StockPriceDto>> getStockPrice(StockPriceParam stockPriceParam) {
        String code = stockPriceParam.getCode();
        LocalDate start = stockPriceParam.getStart();
        LocalDate end = stockPriceParam.getEnd();
        return ResponseEntity.ok(stockService.getStockPrice(code, start, end));
    }


}
