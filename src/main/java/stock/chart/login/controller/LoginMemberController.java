package stock.chart.login.controller;


import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stock.chart.login.dto.LoginMemberForm;
import stock.chart.login.service.LoginMemberService;
import stock.chart.security.dto.TokenInfo;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginMemberController {

    @Value("${cors.frontend}")
    private String referer;

    @Value("${jwt.refresh-expired}")
    private Long refreshTokenExpired;

    private final LoginMemberService loginMemberService;
    private static final String BEARER = "Bearer ";

    /**
     * 로그인, 성공 200, Form 에러 400, 회원 없거나 비밀번호 불일치 404
     */
    @PostMapping("/member")
    public ResponseEntity<TokenInfo> login(@Validated @RequestBody LoginMemberForm loginMemberForm
        , BindingResult bindingResult, HttpServletResponse response) {
        // loginMemberForm에 대한 검증 , 400 에러
        TokenInfo token = loginMemberService.login(loginMemberForm);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie",makeRefreshtokenHeader(token.getRefreshToken()));
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(token);
    }

    /**
     * 카카오 로그인, 성공 200, 실패 403 (가입된 이메일이 없을 경우)
     */
    @GetMapping("/kakao")
    public ResponseEntity<TokenInfo> kakaoLogin(@Param("code") String code, HttpServletResponse response) {
        log.info("code: {}", code);
        TokenInfo token = loginMemberService.kakaoLogin(code);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie",makeRefreshtokenHeader(token.getRefreshToken()));
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(token);
    }

    @PostMapping("/test")
    public String test() {
        return "test";
    }

    /**
     * 토큰 재발급, 성공 200, 실패 400, 사용된 토큰 401
     */
    @GetMapping("/token/reissue")
    public ResponseEntity<TokenInfo> reissueAccessToken(@RequestAttribute("memberId") Long memberId, @RequestAttribute("refreshToken") String refreshToken) {
        TokenInfo token = loginMemberService.reissue(refreshToken, memberId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie",makeRefreshtokenHeader(token.getRefreshToken()));
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(token);
    }

    public String makeRefreshtokenHeader(String refreshToken) {
        return "refreshToken=" + refreshToken + "; Path=/; HttpOnly; Secure; Max-Age=" + refreshTokenExpired;
    }

}
