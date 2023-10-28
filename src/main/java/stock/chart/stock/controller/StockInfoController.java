package stock.chart.stock.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stock.chart.stock.service.StockService;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
public class StockInfoController {
    private final StockService stockService;

    @GetMapping("/search")
    public Object getStockName(String code) {
        try{
            return stockService.getStockName(code);
        }catch (RuntimeException e){
            //검색한 코드가 없을 때 보내는 no_content 에러?
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping("/info")
    public Object getStock(String code) {
        try{
            return stockService.getStock(code);
        }catch (RuntimeException e){
            //검색한 코드가 없을 때 보내는 no_content 에러?
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }


    @GetMapping("/price")
    public Object getStockPrice(String code, @DateTimeFormat(pattern = "yyyyMMdd") LocalDate start, @DateTimeFormat(pattern = "yyyyMMdd") LocalDate end) {
        try{
            //list로 return
            return stockService.getStockPrice(code, start, end);
        }catch (RuntimeException e){
            //검색한 코드가 없을 때 보내는 no_content 에러?
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
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


    @GetMapping("/redis")
    public Object getRedis(String code, @DateTimeFormat(pattern = "yyyyMMdd") LocalDate start, @DateTimeFormat(pattern = "yyyyMMdd") LocalDate end) {
        try{
            //list로 return
            return stockService.getRedis(code, start, end);
        }catch (RuntimeException e){
            //검색한 코드가 없을 때 보내는 no_content 에러?
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }


    @GetMapping("/mysql")
    public Object getMysql(String code, @DateTimeFormat(pattern = "yyyyMMdd") LocalDate start, @DateTimeFormat(pattern = "yyyyMMdd") LocalDate end) {
        try{
            //list로 return
            return stockService.getMysql(code, start, end);
        }catch (RuntimeException e){
            //검색한 코드가 없을 때 보내는 no_content 에러?
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
