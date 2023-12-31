package stock.chart.stock.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stock.chart.stock.dto.PopularStockPriceListDto;
import stock.chart.stock.dto.StockPriceDto;
import stock.chart.stock.dto.StockPriceListDto;
import stock.chart.stock.dto.StockPriceRequestForm;
import stock.chart.stock.exception.IllegalStockException;
import stock.chart.kafka.KafkaProducer;
import stock.chart.stock.repository.StockPriceRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockPriceService {

    private final CacheStockPriceService cacheStockPriceService;
    private final StockPriceRepository stockPriceRepository;
    private final SearchLogService searchLogService;
    private final KafkaProducer kafkaProducer;

    /**
     * 캐시에서 데이터를 가져오고, 캐시에 데이터가 없으면 DB에서 데이터를 가져온다.
     * @param stockPriceRequestForm 요청 폼 containing stock code, start date, end date
     * @return 주식 가격 리스트
     */
    public StockPriceListDto getStockPrice(StockPriceRequestForm stockPriceRequestForm) {
        String message = stockPriceRequestForm.getCode() + "," + UUID.randomUUID().toString();
        kafkaProducer.sendMessage("stock", message);
        return getStockPriceListFromCacheOrRdb(stockPriceRequestForm);
    }

    /**
     * 캐시에서 데이터를 가져온다.
     * 캐시에 데이터가 없거나, RuntimeError가 발생하면 empty를 반환한다.
     */
    private Optional<StockPriceListDto> getFromCache(StockPriceRequestForm stockPriceRequestForm) {
        try {
            return cacheStockPriceService.getCachedStockPriceListDto(stockPriceRequestForm);
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    public PopularStockPriceListDto getPopularStockPriceList() {
        List<StockPriceListDto> stockPriceListDtos = searchLogService.getTodayTop3()
            .stream()
            .map(searchLogSumDto -> getLatestDayPricesOf90(searchLogSumDto.getCode()))
            .collect(Collectors.toList());
        return PopularStockPriceListDto.builder()
            .popularStockList(stockPriceListDtos)
            .build();
    }

    private StockPriceListDto getLatestDayPricesOf90(String code) {
        StockPriceRequestForm stockPriceRequestForm = StockPriceRequestForm.builder()
            .code(code)
            .start(LocalDate.now().minusDays(90))
            .end(LocalDate.now())
            .build();
        return getStockPriceListFromCacheOrRdb(stockPriceRequestForm);
    }

    private StockPriceListDto getStockPriceListFromCacheOrRdb(StockPriceRequestForm stockPriceRequestForm) {
        Optional<StockPriceListDto> cachedStockPriceListDto = getFromCache(stockPriceRequestForm);
        if (cachedStockPriceListDto.isPresent()) {
            return cachedStockPriceListDto.get();
        }
        List<StockPriceDto> collect = stockPriceRepository.findAllWithDate(stockPriceRequestForm.getCode(),
            stockPriceRequestForm.getStart(), stockPriceRequestForm.getEnd())
            .orElseThrow(IllegalStockException::new)
            .stream()
            .map(StockPriceDto::new)
            .collect(Collectors.toList());
        return StockPriceListDto.builder()
            .stockCode(stockPriceRequestForm.getCode())
            .stockPriceDtoList(collect)
            .build();
    }
}
