package stock.chart.login.exception;

/**
 * Oauth 로그인 중 일치하는 이메일이 가입이 안되어 있을 경우 발생하는 에러
 */
public class NeedRegisterMemberException extends RuntimeException {

    public NeedRegisterMemberException(String email) {
        super(email);
    }
}
