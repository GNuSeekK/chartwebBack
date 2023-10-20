package stock.chart.member.exception;

public class PasswordConfirmException extends RuntimeException{

        public PasswordConfirmException() {
            super("비밀번호가 일치하지 않습니다.");
        }

}
