package stock.chart.news.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import stock.chart.news.dto.NewsApiResponseDto;
import stock.chart.news.dto.NewsResponseDto;
import stock.chart.news.exception.NewsNullException;
import stock.chart.news.repository.NewsApiResponseRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsService {

    private final NewsApiResponseRepository newsApiResponseRepository;
    private final RedisImageCashService redisImageCashService;

    public List<NewsResponseDto> getNaverNewsWithKeyword(String keyword) {
        NewsApiResponseDto newsApiResponseDto = newsApiResponseRepository.getNewsApiResponseDto(keyword)
            .orElseThrow(NewsNullException::new)
            .getOnlyNaverNews();
        List<NewsResponseDto> cashedResponse = redisImageCashService.getNewsResponseDtoList(newsApiResponseDto);
        List<NewsResponseDto> cralwedResponse = cashedResponse.stream()
            .map(NewsResponseDto::crawlingImage) // 크롤링 수행
            .collect(Collectors.toList());
        saveRedisImageCash(cralwedResponse);
        return cralwedResponse.stream()
            .filter(NewsResponseDto::isHavingImage)
            .collect(Collectors.toList());
    }

    private void saveRedisImageCash(List<NewsResponseDto> cralwedResponse) {
        cralwedResponse.forEach(newsResponseDto -> redisImageCashService.saveRedisImageCash(newsResponseDto.getLink(),
            newsResponseDto.getImageLink()));
    }
}
