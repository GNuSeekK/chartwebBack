package stock.chart.member.exception;

public class InvalidMemberException extends RuntimeException {
    public InvalidMemberException() {
        super("유효하지 않은 회원입니다.");
    }
}
