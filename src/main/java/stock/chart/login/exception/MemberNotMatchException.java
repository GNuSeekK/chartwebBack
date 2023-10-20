package stock.chart.login.exception;

public class MemberNotMatchException extends RuntimeException {

    public MemberNotMatchException() {
        super("이메일 또는 비밀번호가 일치하지 않습니다.");
    }
}
