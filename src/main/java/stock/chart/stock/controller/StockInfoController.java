package stock.chart.stock.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stock.chart.domain.Stock;
import stock.chart.stock.dto.StockDataDto;
import stock.chart.stock.dto.StockPriceDto;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import stock.chart.domain.Stock;
import stock.chart.domain.StockPrice;
import stock.chart.stock.dto.StockDataDto;
import stock.chart.stock.dto.StockPriceDto;
import stock.chart.stock.dto.StockPriceParam;
import stock.chart.stock.exception.StockNotFoundException;
import stock.chart.stock.service.StockService;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
public class StockInfoController {
    private final StockService stockService;

    /**
     * 성공 200, 400 파라미터 없음, 404 존재하지 않는 주식
     */
    @GetMapping("/search")
    public ResponseEntity<StockDataDto> getStockName(@RequestParam String code) {
            return ResponseEntity.ok().body(stockService.getStockName(code));
    }

    /**
     * 성공 200, 400 파라미터 없음, 404 존재하지 않는 주식
     */
    @GetMapping("/info")
    public ResponseEntity<Stock> getStock(@RequestParam String code) {
        return ResponseEntity.ok().body(stockService.getStock(code));
    }
    /**
     * 성공 200(날짜 없으면 자동으로 1년데이터 return), 400 주식 코드 없음, 404 존재하지 않는 주식
     */
    @GetMapping("/price")
    public ResponseEntity<List<StockPriceDto>> getStockPrice(@Valid @ModelAttribute StockPriceParam stockPriceParam) {
        return ResponseEntity.ok().body(stockService.getStockPrice(stockPriceParam));
    }


    @GetMapping("/price/mysql")
    public Object getStockPriceMySQL(String code, @DateTimeFormat(pattern = "yyyyMMdd") LocalDate start, @DateTimeFormat(pattern = "yyyyMMdd") LocalDate end) {
        try{
            //list로 return
            return stockService.getStockPriceMySQL(code, start, end);
        }catch (RuntimeException e){
            //검색한 코드가 없을 때 보내는 no_content 에러?
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
