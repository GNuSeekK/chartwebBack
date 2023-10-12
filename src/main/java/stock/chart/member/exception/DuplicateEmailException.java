package stock.chart.member.exception;

public class DuplicateEmailException extends DuplicateMemberException {

    public DuplicateEmailException() {
        super("이미 존재하는 이메일입니다.", "email");
    }
}
