package stock.chart.news.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsApiResponseDto {

    @JsonSetter("items")
    private List<NewsSummaryDto> items;

    public NewsApiResponseDto getOnlyNaverNews() {
        return NewsApiResponseDto.builder()
            .items(items.stream()
                .filter(item -> item.getLink().startsWith("https://n.news"))
                .collect(Collectors.toList()))
            .build();
    }
}
