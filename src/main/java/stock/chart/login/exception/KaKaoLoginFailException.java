package stock.chart.login.exception;

public class KaKaoLoginFailException extends RuntimeException {

    public KaKaoLoginFailException() {
        super("카카오 로그인 실패");
    }

}
