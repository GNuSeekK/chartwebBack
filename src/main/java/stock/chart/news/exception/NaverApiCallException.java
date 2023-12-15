package stock.chart.news.exception;

public class NaverApiCallException extends RuntimeException {


    public NaverApiCallException() {
        super("Naver API 호출 중 오류가 발생했습니다.");
    }
}
