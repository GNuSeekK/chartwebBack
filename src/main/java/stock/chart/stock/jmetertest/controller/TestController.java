package stock.chart.stock.jmetertest.controller;


import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stock.chart.stock.dto.StockPriceDto;
import stock.chart.stock.jmetertest.service.TestService;
import stock.chart.stock.service.StockService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TestService stockService;
    private final StockService realStockService;

    @GetMapping("/save")
    public ResponseEntity<String> saveStockPrice(String code) {
        stockService.saveStockPriceValue(code);
        return ResponseEntity.ok("저장 완료");
    }

    @GetMapping("/save/sorted")
    public ResponseEntity<String> saveStockPriceSorted(String code) {
        stockService.saveStockPriceValueSorted(code);
        return ResponseEntity.ok("저장 완료");
    }

    @GetMapping("/price/sorted/mysql")
    public ResponseEntity<List<StockPriceDto>> getSortedPriceMysql(String code, @DateTimeFormat(pattern = "yyyyMMdd") LocalDate start, @DateTimeFormat(pattern = "yyyyMMdd") LocalDate end) {
        return ResponseEntity.ok(stockService.getSortedStockPriceFromMysql(code, start, end));
    }

    @GetMapping("/price/sorted/redis")
    public ResponseEntity<List<StockPriceDto>> getSortedPriceRedis(String code, @DateTimeFormat(pattern = "yyyyMMdd") LocalDate start, @DateTimeFormat(pattern = "yyyyMMdd") LocalDate end) {
        return ResponseEntity.ok(stockService.getSortedStockPriceFromRedis(code, start, end));
    }

    @GetMapping("/mysql")
    public ResponseEntity<Object> getStockPriceFromMysql(String code) {
        return ResponseEntity.ok(stockService.getStockPriceFromMysql(code));
    }

    @GetMapping("/redis")
    public ResponseEntity<Object> getStockPriceFromRedis(String code) {
        return ResponseEntity.ok(stockService.getStockPriceValueFromRedis(code));
    }

    @GetMapping("/savesome")
    public ResponseEntity<String> saveSomeStockPrice(String code, @DateTimeFormat(pattern = "yyyyMMdd") LocalDate start, @DateTimeFormat(pattern = "yyyyMMdd") LocalDate end) {
        stockService.saveSomeStockPrice(code, start, end);
        return ResponseEntity.ok("저장 완료");
    }

    @GetMapping("/alldata")
    public ResponseEntity<String> allStockData(String code) {
        stockService.allStockData(code);
        return ResponseEntity.ok("쿼리 수행 완료");
    }

    @GetMapping("/price/mysql")
    public ResponseEntity<List<StockPriceDto>> getStockPriceMySQL(String code, @DateTimeFormat(pattern = "yyyyMMdd") LocalDate start, @DateTimeFormat(pattern = "yyyyMMdd") LocalDate end) {
        return ResponseEntity.ok(stockService.getStockPriceRdbms(code, start, end));
    }

}
