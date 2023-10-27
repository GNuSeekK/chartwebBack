package stock.chart.stock.service;

import java.time.LocalDate;
import java.util.List;
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

    private Long EXPIRATION = 10L;

    public StockDataDto getStockName(String code) {
        return stockRepository.findStockNameById(code)
                .orElseThrow(StockNotFoundException::new);
    }

    public Stock getStock(String code){
        return stockRepository.findStockById(code)
                .orElseThrow(StockNotFoundException::new);
    }

    public List<StockPriceDto> getStockPrice(String code, LocalDate start, LocalDate end) {

        List<StockPriceDto> redisData = checkStockCashing(code, start, end);
        if (redisData != null) {
            return redisData;
        }
        List<StockPriceDto> stock = updateStockCashing(code, start, end);
        if (stock != null) {
            return stock;
        }
        List<StockPrice> stockPrices = stockPriceRepository.findAll(code, start, end)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."));
        return stockPrices.stream()
            .map(StockPrice::toStockPriceDto)
            .collect(Collectors.toList());
    }

    private List<StockPriceDto> updateStockCashing(String code, LocalDate start, LocalDate end) {
        Optional<StockCashPriority> cashPriority = stockCashPriorityRepository.findByCode(code);
        if (cashPriority.isPresent()) { // 우선순위에 들어가 있으면
            if (cashPriority.get().getPriority() == 1) {
                log.info("우선순위가 1입니다.");
                // 우선순위가 1이면 redis에 저장
                Stock stock = stockRepository.findStockByIdWithStockPrices(code)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."));
                redisStockRepository.save(CashStock.builder()
                    .code(code)
                    .name(stock.getName())
                    .cashStockPriceList(stock.getStockPrices().stream()
                        .map(StockPrice::toCashStockPrice)
                        .collect(Collectors.toList()))
                    .build());
                return stock.getStockPrices().stream()
                    .filter(stockPrice -> stockPrice.getId().getDate().isAfter(start.minusDays(1)) && stockPrice.getId()
                        .getDate().isBefore(end.plusDays(1)))
                    .map(StockPrice::toStockPriceDto)
                    .collect(Collectors.toList());
            } else {
                // 우선순위가 1이 아니면 우선순위와 만료시간 업데이트
                log.info("우선순위가 1이 아닙니다.");
                cashPriority.get().updatePriorityAndExpiration(EXPIRATION);
                // save cashPriority
                stockCashPriorityRepository.save(cashPriority.get());
            }
        } else {
            log.info("우선순위에 없습니다.");
            // 우선순위에 없을 때 -> 우선순위 5로 저장
            stockCashPriorityRepository.save(StockCashPriority.builder()
                .code(code)
                .priority(5)
                .expiration(EXPIRATION)
                .build());
        }
        return null;
    }

    private List<StockPriceDto> checkStockCashing(String code, LocalDate start, LocalDate end) {
        Optional<CashStock> redisData = redisStockRepository.findByCode(code);
        if (redisData.isPresent()) { // 데이터가 레디스에 있으면, 바로 리턴
            LocalDate newStart = start.minusDays(1);
            LocalDate newEnd = end.plusDays(1);
            return redisData.get().getStockPrices().stream()
                .filter(stockPriceDto -> stockPriceDto.getDate().isAfter(newStart) && stockPriceDto.getDate()
                    .isBefore(newEnd))
                .collect(Collectors.toList());
        }
        return null;
    public List<StockPriceDto> getStockPrice(StockPriceParam stockPriceParam) {
        String code = stockPriceParam.getCode();
        LocalDate start = stockPriceParam.getStart();
        LocalDate end = stockPriceParam.getEnd();

        //주식코드 검증
        getStock(code);

        if(start == null || end == null){ //데이터가 없으면 최근 1년 정보
            end = LocalDate.now();
            start = end.minusYears(1);
        }
        return stockPriceRepository.findAll(code, start, end)
                .orElseThrow(StockNotFoundException::new);
    }
}
