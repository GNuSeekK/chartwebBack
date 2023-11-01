package stock.chart.stock.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stock.chart.domain.Stock;
import stock.chart.domain.StockPrice;
import stock.chart.domain.redis.CashStock;
import stock.chart.domain.redis.StockCashPriority;
import stock.chart.stock.dto.StockDataDto;
import stock.chart.stock.dto.StockPriceDto;
import stock.chart.stock.exception.IllegalStockException;
import stock.chart.stock.repository.RedisStockRepository;
import stock.chart.stock.repository.StockCashPriorityRepository;
import stock.chart.stock.repository.StockPriceRepository;
import stock.chart.stock.repository.StockRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private final StockRepository stockRepository;
    private final StockPriceRepository stockPriceRepository;
    private final RedisStockRepository redisStockRepository;
    private final StockCashPriorityRepository stockCashPriorityRepository;

    private static final int MAX_PRIORITY = 5;


    public StockDataDto getStockName(String code) {
        return stockRepository.findStockNameById(code)
            .orElseThrow(IllegalStockException::new);
    }

    public Stock getStock(String code) {
        return stockRepository.findStockById(code)
            .orElseThrow(IllegalStockException::new);
    }

    /**
     * update가 redis에만 일어나므로 transaction readOnly = true
     */
    public List<StockPriceDto> getStockPrice(String code, LocalDate start, LocalDate end) {

        Optional<Integer> savedFlag = stockCashPriorityRepository.getSavedFlag(code);
        if (savedFlag.isPresent() && savedFlag.get() == 1) {
            log.info("레디스에 저장되어 있습니다.");
            Optional<List<StockPriceDto>> redisData = checkStockCashing(code, start, end);
            if (redisData.isPresent()) {
                return redisData.get();
            }
            stockCashPriorityRepository.invalidateSaveFlag(code);
        }
        Optional<List<StockPriceDto>> stock = updateStockCashing(code, start, end);
        if (stock.isPresent()) {
            return stock.get();
        }
        List<StockPrice> stockPrices = stockPriceRepository.findAll(code, start, end)
            .orElseThrow(IllegalStockException::new);
        return stockPrices.parallelStream()
            .map(StockPrice::toStockPriceDto)
            .collect(Collectors.toList());
    }

    private Optional<List<StockPriceDto>> updateStockCashing(String code, LocalDate start, LocalDate end) {
        Optional<Integer> priority = stockCashPriorityRepository.getPriority(code);
        if (priority.isPresent()) { // 우선순위에 들어가 있으면
            if (priority.get() < 1) {
                log.info("우선순위가 1입니다.");
                stockCashPriorityRepository.updatePriorityAndExpiration(code);
                Stock stock = stockRepository.findStockByIdWithStockPrices(code)
                    .orElseThrow(IllegalStockException::new);
                Optional<Integer> saveFlag = stockCashPriorityRepository.getSavedFlag(code);
                if (saveFlag.isPresent() && saveFlag.get() == 1) {
                    List<StockPriceDto> redisData = checkStockCashing(code, start, end).orElse(null);
                    if (redisData != null) {
                        return Optional.of(redisData);
                    }
                }
                Optional<Integer> savingFlag = stockCashPriorityRepository.getLockFlag(code);
                if (savingFlag.isEmpty()) { // 우선순위가 1보다 작고, 저장중인게 없으면
                    redisStockRepository.saveSortedSet(code, stock.getStockPrices().stream()
                        .map(StockPrice::toCashStockPrice)
                        .collect(Collectors.toSet()));
                }
                return Optional.of(stock.getStockPrices().parallelStream()
                    .filter(stockPrice -> stockPrice.getId().getDate().isAfter(start.minusDays(1)) && stockPrice.getId()
                        .getDate().isBefore(end.plusDays(1)))
                    .map(StockPrice::toStockPriceDto)
                    .collect(Collectors.toList()));
            } else {
                // 우선순위가 1보다 크면
                log.info("우선순위가 1보다 큽니다.");
                // 우선순위 1 감소
                stockCashPriorityRepository.updatePriorityAndExpiration(code);
            }
        } else {
            log.info("우선순위에 없습니다.");
            // 우선순위에 없을 때 -> 우선순위 5로 저장
            stockCashPriorityRepository.save(StockCashPriority.builder()
                .code(code)
                .priority(MAX_PRIORITY)
                .build());
        }
        return Optional.empty();
    }

    private Optional<List<StockPriceDto>> checkStockCashing(String code, LocalDate start, LocalDate end) {
        Optional<CashStock> redisData = redisStockRepository.getCashStockWithSortedStockPrice(code, start, end);
        // 데이터가 레디스에 있으면, 바로 리턴
        List<StockPriceDto> result = redisData.map(cashStock -> new ArrayList<>(cashStock.getStockPrices()))
            .orElse(null);
        if (result != null) {
            return Optional.of(result);
        }
        return Optional.empty();
    }


}
