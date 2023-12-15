package stock.chart.news.exception;

public class NewsNullException extends RuntimeException {

    public NewsNullException() {
        super("해당 키워드에 대한 뉴스가 존재하지 않습니다.");
    }

}
