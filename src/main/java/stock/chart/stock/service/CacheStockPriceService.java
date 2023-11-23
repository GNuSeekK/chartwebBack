package stock.chart.stock.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import stock.chart.redis.service.CacheCompleteService;
import stock.chart.stock.dto.StockPriceDto;
import stock.chart.stock.dto.StockPriceListDto;
import stock.chart.stock.dto.StockPriceRequestForm;
import stock.chart.stock.entity.CacheStockPrice;
import stock.chart.stock.repository.CacheStockPriceRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheStockPriceService {

    private final CacheStockPriceRepository cacheStockPriceRepository;
    private final CacheCompleteService cacheCompleteService;

    /**
     * 캐시된 데이터를 가져온다.
     * 캐시된 데이터가 없으면 empty를 반환한다.
     * @param stockPriceRequestForm 요청 폼 containing stock code, start date, end date
     * @return 캐시된 주식 가격 리스트
     */
    public Optional<StockPriceListDto> getCachedStockPriceListDto(StockPriceRequestForm stockPriceRequestForm) {
        if (!cacheCompleteService.isCached(stockPriceRequestForm.getCode())) {
            return Optional.empty();
        }
        Optional<StockPriceListDto> stockPriceListDto = cacheStockPriceRepository.getDataWithDate(
                stockPriceRequestForm.getCode(),
                stockPriceRequestForm.getStart(), stockPriceRequestForm.getEnd())
            .map(cacheStockPrices -> makeStockPriceListDto(stockPriceRequestForm, cacheStockPrices));
        if (stockPriceListDto.isPresent()) {
            return stockPriceListDto;
        }
        cacheCompleteService.deleteComplete(stockPriceRequestForm.getCode());
        return Optional.empty();
    }

    private StockPriceListDto makeStockPriceListDto(StockPriceRequestForm stockPriceRequestForm,
        Set<CacheStockPrice> item) {
        if (item.isEmpty()) {
            return null;
        }
        return StockPriceListDto.builder()
            .stockCode(stockPriceRequestForm.getCode())
            .stockPriceDtoList(item.stream()
                .map(StockPriceDto::new)
                .collect(Collectors.toList()))
            .build();
    }

}
