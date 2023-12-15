package stock.chart.utils;

import java.io.IOException;
import java.util.Optional;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class JsoupCrawling {

    public Connection getConnection(String url) {
        return Jsoup.connect(url);
    }

    public Optional<Elements> getJsoupElements(String url, String query) {

        Connection conn = getConnection(url);
        try {
            return Optional.of(conn.get().select(query));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

}
