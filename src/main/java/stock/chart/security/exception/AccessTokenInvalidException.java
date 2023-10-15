package stock.chart.security.exception;

public class AccessTokenInvalidException extends RuntimeException{

    public AccessTokenInvalidException() {
        super("유효하지 않은 토큰입니다.");
    }
}
