package stock.chart.login.exception;

public class UsedTokenException extends RuntimeException {

    public UsedTokenException() {
        super("이미 사용된 토큰입니다.");
    }
}
