package stock.chart.stock.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stock.chart.member.service.MemberService;
import stock.chart.stock.service.StockService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
public class StockInfoController {
    private final StockService stockService;

    @GetMapping("/info")
    public Object getStockInfo(String code) {
        try{
            return stockService.getStockInfo(code);
        }catch (RuntimeException e){
            //검색한 코드가 없을 때 보내는 no_content 에러?
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

    }


}
