package stock.chart.news.dto;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.jsoup.select.Elements;
import stock.chart.utils.JsoupCrawling;

@Getter
@Builder
@AllArgsConstructor
public class NewsResponseDto {

    private String title;
    private String link;
    private String description;
    private String imageLink;

    public boolean isHavingImage() {
        return validateNullImageLink() && !imageLink.isEmpty();
    }

    public boolean validateNullImageLink() {
        return imageLink != null;
    }

    public NewsResponseDto crawlingImage() {
        if (validateNullImageLink()) {
            return this;
        }
        return NewsResponseDto.builder()
            .imageLink(crawlingImageLink())
            .title(title)
            .link(link)
            .description(description)
            .build();
    }

    private String crawlingImageLink() {
        JsoupCrawling jsoupCrawling = new JsoupCrawling();
        String query = "#contents img";
        Optional<Elements> jsoupElements = jsoupCrawling.getJsoupElements(link, query);
        String imageLink = null;
        if (jsoupElements.isPresent()) {
            imageLink = jsoupElements.get().attr("data-src");
        }
        return imageLink;
    }
}
