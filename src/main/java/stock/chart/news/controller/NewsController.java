package stock.chart.news.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import stock.chart.news.dto.NewsResponseDto;
import stock.chart.news.service.NewsService;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;
    @GetMapping
    public ResponseEntity<List<NewsResponseDto>> getNewsWithKeyword(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(newsService.getNaverNewsWithKeyword(keyword));
    }

}
