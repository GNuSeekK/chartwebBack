package stock.chart.news.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import stock.chart.news.dto.NewsApiResponseDto;
import stock.chart.news.dto.NewsResponseDto;
import stock.chart.news.entity.RedisImageCash;
import stock.chart.news.repository.RedisImageCashRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisImageCashService {

    private final RedisImageCashRepository redisImageCashRepository;

    public List<NewsResponseDto> getNewsResponseDtoList(NewsApiResponseDto newsApiResponseDto) {
        return newsApiResponseDto.getItems().stream()
            .map(item -> NewsResponseDto.builder()
                .title(item.getTitle())
                .link(item.getLink())
                .description(item.getDescription())
                .imageLink(redisImageCashRepository.findById(item.getLink()).orElse(new RedisImageCash()).getImageUrl())
                .build())
            .collect(Collectors.toList());
    }

    public void saveRedisImageCash(String link, String imageLink) {
        if (redisImageCashRepository.findById(link).isPresent()) {
            return;
        }
        redisImageCashRepository.save(RedisImageCash.builder()
            .url(link)
            .imageUrl(imageLink)
            .build());
    }
}
