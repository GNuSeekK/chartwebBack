package stock.chart.news.repository;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import stock.chart.news.dto.NewsApiResponseDto;
import stock.chart.news.exception.NaverApiCallException;

@Repository
public class NewsApiResponseRepository {

    @Value("${news.api.secret}")
    private String newsApiKey;
    @Value("${news.api.client}")
    private String newsApiClient;

    public Optional<NewsApiResponseDto> getNewsApiResponseDto(String keyword) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getNaverApiRequestHeaders();

        String url = "https://openapi.naver.com/v1/search/news.json?query=" + keyword + "&display=100";
        ResponseEntity<NewsApiResponseDto> newsApiResponseDtoEntity = restTemplate.exchange(url, HttpMethod.GET,
            new HttpEntity<>(headers), NewsApiResponseDto.class);
        if (newsApiResponseDtoEntity.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(newsApiResponseDtoEntity.getBody());
        }
        throw new NaverApiCallException();
    }

    private HttpHeaders getNaverApiRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", newsApiClient);
        headers.set("X-Naver-Client-Secret", newsApiKey);
        return headers;
    }

}
