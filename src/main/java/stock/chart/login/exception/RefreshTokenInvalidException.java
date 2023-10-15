package stock.chart.login.exception;

public class RefreshTokenInvalidException extends RuntimeException {

        public RefreshTokenInvalidException() {
            super("Refresh Token이 유효하지 않습니다.");
        }

}
