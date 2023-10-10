package stock.chart.login.controller;

import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;
import stock.chart.login.dto.LoginMemberRequestDto;
import stock.chart.login.service.LoginMemberService;
import stock.chart.security.dto.TokenInfo;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginMemberController {

    @Value("${cors.frontend}")
    private String referer;

    private Long refreshTokenExpired;

    private final LoginMemberService loginMemberService;

    @PostMapping("/member")
    public TokenInfo loginTest(@RequestBody LoginMemberRequestDto loginMemberRequestDto
        , HttpServletResponse response) throws IOException {
        log.info("memberLoginRequestDto: {}", loginMemberRequestDto);
        TokenInfo token = loginMemberService.login(loginMemberRequestDto);
        log.info("token: {}", token);
        return token;
    }

    @PostMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/token/reissue")
    public Object reissueAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            log.info("cookie: {}", cookie);
        }

        log.info("reissueAccessToken start");
        Cookie refreshToken = WebUtils.getCookie(request, "refreshToken");
        log.info("refreshToken: {}", refreshToken);
        Object responseJson = loginMemberService.reissue(refreshToken.getValue());
        log.info("tokenInfo: {}", responseJson);
        return responseJson;
    }


}
