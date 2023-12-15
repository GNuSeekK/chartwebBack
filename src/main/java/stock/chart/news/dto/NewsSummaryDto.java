package stock.chart.news.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;

@Getter
public class NewsSummaryDto {

    @JsonSetter("title")
    private String title;
    @JsonSetter("link")
    private String link;
    @JsonSetter("description")
    private String description;

}
