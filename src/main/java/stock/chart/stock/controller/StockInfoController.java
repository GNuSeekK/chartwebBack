package stock.chart.stock.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stock.chart.stock.dto.StockPriceListDto;
import stock.chart.stock.dto.StockPriceRequestForm;
import stock.chart.stock.service.StockPriceService;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
public class StockInfoController {

    private final StockPriceService stockPriceService;

    @GetMapping("/price")
    public ResponseEntity<StockPriceListDto> getStockPrice(@Valid StockPriceRequestForm stockPriceRequestForm) {
        stockPriceRequestForm.inputDefaultDate();
        return ResponseEntity.ok(stockPriceService.getStockPrice(stockPriceRequestForm));
    }
}
