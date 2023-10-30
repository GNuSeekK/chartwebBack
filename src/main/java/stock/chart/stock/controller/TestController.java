package stock.chart.stock.controller;


import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stock.chart.stock.service.TestService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TestService stockService;


    @GetMapping("/save")
    public ResponseEntity<String> saveStockPrice(String code) {
//        stockService.saveStockPrice(code);
        stockService.saveStockPriceValue(code);
        return ResponseEntity.ok("저장 완료");
    }

    @GetMapping("/mysql")
    public ResponseEntity<Object> getStockPriceFromMysql(String code) {
        return ResponseEntity.ok(stockService.getStockPriceFromMysql(code));
    }

    @GetMapping("/redis")
    public ResponseEntity<Object> getStockPriceFromRedis(String code) {
//        return ResponseEntity.ok(stockService.getStockPriceFromRedis(code));
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


}
