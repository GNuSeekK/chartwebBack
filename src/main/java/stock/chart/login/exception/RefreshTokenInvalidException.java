package stock.chart.login.exception;

// 저장된 RefreshToken이 Database에 없을 경우
public class RefreshTokenInvalidException extends RuntimeException {

        public RefreshTokenInvalidException() {
            super("Refresh Token이 유효하지 않습니다.");
        }

}
