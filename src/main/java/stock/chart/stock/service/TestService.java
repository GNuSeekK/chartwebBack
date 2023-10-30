package stock.chart.stock.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stock.chart.domain.Stock;
import stock.chart.domain.StockPrice;
import stock.chart.domain.redis.CashStockPrice;
import stock.chart.domain.redis.TestCashStock;
import stock.chart.stock.dto.StockPriceDto;
import stock.chart.stock.repository.StockCashPriorityRepository;
import stock.chart.stock.repository.StockPriceRepository;
import stock.chart.stock.repository.StockRepository;
import stock.chart.stock.repository.TestCashStockRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TestService {

    private final StockRepository stockRepository;
    private final StockPriceRepository stockPriceRepository;
    private final TestCashStockRepository redisStockRepository;
    private final StockCashPriorityRepository stockCashPriorityRepository;

    public void saveStockPrice(String code) {
        Stock stock = stockRepository.findStockByIdWithStockPrices(code)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."));
        List<StockPrice> stockPrices = stock.getStockPrices();
        TestCashStock testCashStock = TestCashStock.builder()
            .code(code)
            .cashStockPricesSet(stockPrices.parallelStream()
                .map(stockPrice -> CashStockPrice.builder()
                    .originalDate(stockPrice.getId().getDate())
                    .open(stockPrice.getOpen())
                    .high(stockPrice.getHigh())
                    .low(stockPrice.getLow())
                    .close(stockPrice.getClose())
                    .volume(stockPrice.getVolume())
                    .build())
                .collect(Collectors.toList()))
            .build();
        redisStockRepository.save(testCashStock);
    }

    public void saveStockPriceValue(String code) {
        Stock stock = stockRepository.findStockByIdWithStockPrices(code)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."));
        List<StockPrice> stockPrices = stock.getStockPrices();
        TestCashStock testCashStock = TestCashStock.builder()
            .code(code)
            .cashStockPricesSet(stockPrices.parallelStream()
                .map(stockPrice -> CashStockPrice.builder()
                    .originalDate(stockPrice.getId().getDate())
                    .open(stockPrice.getOpen())
                    .high(stockPrice.getHigh())
                    .low(stockPrice.getLow())
                    .close(stockPrice.getClose())
                    .volume(stockPrice.getVolume())
                    .build())
                .collect(Collectors.toList()))
            .build();
        redisStockRepository.setCashStockValue(code, testCashStock);
    }

    public List<StockPriceDto> getStockPriceFromMysql(String code) {
        Date date = new Date();
        Stock stock = stockRepository.findStockByIdWithStockPrices(code)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."));
        List<StockPrice> stockPrices = stock.getStockPrices();
        log.info("mysql 조회 시간 : {}", new Date().getTime() - date.getTime());
        return stockPrices.parallelStream()
            .map(stockPrice -> StockPriceDto.builder()
                .code(stockPrice.getId().getCode())
                .date(stockPrice.getId().getDate())
                .open(stockPrice.getOpen())
                .high(stockPrice.getHigh())
                .low(stockPrice.getLow())
                .close(stockPrice.getClose())
                .volume(stockPrice.getVolume())
                .build())
            .collect(Collectors.toList());
    }

    public List<StockPriceDto> getStockPriceFromRedis(String code) {
        Date date = new Date();
        TestCashStock testCashStock = redisStockRepository.findById(code)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."));
        log.info("redis 조회 시간 : {}", new Date().getTime() - date.getTime());
        List<CashStockPrice> cashStockPrices = testCashStock.getCashStockPricesSet();
        return cashStockPrices.parallelStream()
            .map(cashStockPrice -> StockPriceDto.builder()
                .code(code)
                .date(cashStockPrice.getOriginalDate())
                .open(cashStockPrice.getOpen())
                .high(cashStockPrice.getHigh())
                .low(cashStockPrice.getLow())
                .close(cashStockPrice.getClose())
                .volume(cashStockPrice.getVolume())
                .build())
            .collect(Collectors.toList());
    }

    public List<StockPriceDto> getStockPriceValueFromRedis(String code) {
        Date date = new Date();
        TestCashStock testCashStock = redisStockRepository.getCashStockValue(code)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."));
        log.info("redis 조회 시간 : {}", new Date().getTime() - date.getTime());
        List<CashStockPrice> cashStockPrices = testCashStock.getCashStockPricesSet();
        return cashStockPrices.parallelStream()
            .map(cashStockPrice -> StockPriceDto.builder()
                .code(code)
                .date(cashStockPrice.getOriginalDate())
                .open(cashStockPrice.getOpen())
                .high(cashStockPrice.getHigh())
                .low(cashStockPrice.getLow())
                .close(cashStockPrice.getClose())
                .volume(cashStockPrice.getVolume())
                .build())
            .collect(Collectors.toList());
    }

    public void saveSomeStockPrice(String code, LocalDate start, LocalDate end) {
        List<StockPrice> stockPrices = stockPriceRepository.findAll(code, start, end)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."));
        TestCashStock testCashStock = TestCashStock.builder()
            .code(code)
            .cashStockPricesSet(stockPrices.parallelStream()
                .map(stockPrice -> CashStockPrice.builder()
                    .originalDate(stockPrice.getId().getDate())
                    .open(stockPrice.getOpen())
                    .high(stockPrice.getHigh())
                    .low(stockPrice.getLow())
                    .close(stockPrice.getClose())
                    .volume(stockPrice.getVolume())
                    .build())
                .collect(Collectors.toList()))
            .build();
        redisStockRepository.save(testCashStock);
    }

    public void allStockData(String code) {
        stockPriceRepository.findAllForDummy()
            .orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."));
    }
}
