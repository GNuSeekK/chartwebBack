package stock.chart.stock.service;

import java.time.LocalDate;
import java.util.ArrayList;
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
import stock.chart.stock.dto.StockDataDto;
import stock.chart.stock.dto.StockPriceDto;
import stock.chart.stock.dto.StockPriceParam;
import stock.chart.stock.exception.StockNotFoundException;
import stock.chart.stock.dto.StockPriceDto;
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


    public StockDataDto getStockName(String code) {
        return stockRepository.findStockNameById(code)
                .orElseThrow(StockNotFoundException::new);
    }

    public Stock getStock(String code){
        return stockRepository.findStockById(code)
                .orElseThrow(StockNotFoundException::new);
    }

    public List<StockPriceDto> getStockPrice(String code, LocalDate start, LocalDate end) {

        Optional<Integer> saveFlag = stockCashPriorityRepository.getSaveFlag(code);
        if (saveFlag.isPresent() && saveFlag.get() == 1) {
            log.info("레디스에 저장되어 있습니다.");
            List<StockPriceDto> redisData = checkStockCashing(code, start, end);
            return Objects.requireNonNullElseGet(redisData, () -> stockPriceRepository.findAll(code, start, end)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."))
                .parallelStream()
                .map(StockPrice::toStockPriceDto)
                .collect(Collectors.toList()));
        }
        List<StockPriceDto> stock = updateStockCashing(code, start, end);
        if (stock != null) {
            return stock;
        }
        List<StockPrice> stockPrices = stockPriceRepository.findAll(code, start, end)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."));
        return stockPrices.parallelStream()
            .map(StockPrice::toStockPriceDto)
            .collect(Collectors.toList());
    }

    private List<StockPriceDto> updateStockCashing(String code,
        LocalDate start, LocalDate end) {
        Optional<Integer> priority = stockCashPriorityRepository.getPriority(code);
        if (priority.isPresent()) { // 우선순위에 들어가 있으면
            if (priority.get() < 1) {
                log.info("우선순위가 1입니다.");
                stockCashPriorityRepository.updatePriorityAndExpiration(code);
                Stock stock = stockRepository.findStockByIdWithStockPrices(code)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."));
                Optional<Integer> saveFlag = stockCashPriorityRepository.getSaveFlag(code);
                if (saveFlag.isPresent() && saveFlag.get() == 1) {
                    List<StockPriceDto> redisData = checkStockCashing(code, start, end);
                    if (redisData != null) {
                        return redisData;
                    }
                }
                redisStockRepository.saveSortedSet(code, stock.getStockPrices().stream()
                    .map(StockPrice::toCashStockPrice)
                    .collect(Collectors.toSet()));
                return stock.getStockPrices().parallelStream()
                    .filter(stockPrice -> stockPrice.getId().getDate().isAfter(start.minusDays(1)) && stockPrice.getId()
                        .getDate().isBefore(end.plusDays(1)))
                    .map(StockPrice::toStockPriceDto)
                    .collect(Collectors.toList());
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
                .priority(5)
                .build());
        }
        return null;
    }

    private List<StockPriceDto> checkStockCashing(String code, LocalDate start, LocalDate end) {
        Optional<CashStock> redisData = redisStockRepository.getCashStockWithSortedStockPrice(code, start, end);
        // 데이터가 레디스에 있으면, 바로 리턴
        return redisData.map(cashStock -> new ArrayList<>(cashStock.getStockPrices())).orElse(null);
    }
}
