package stock.chart.login.controller;

import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;
import stock.chart.login.dto.LoginMemberForm;
import stock.chart.login.exception.MemberNotMatchException;
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
    public ResponseEntity loginTest(@Validated @RequestBody LoginMemberForm loginMemberForm
        , BindingResult bindingResult, HttpServletResponse response) throws IOException {

        // loginMemberForm에 대한 검증 , 400 에러
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        TokenInfo token = null;
        try {
            token = loginMemberService.login(loginMemberForm);
        } catch (MemberNotMatchException e) {
            bindingResult.addError(new ObjectError("LoginMemberForm", e.getMessage()));
        }

        // 이메일, 비밀번호 유효성 검증, 404 에러
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bindingResult.getAllErrors());
        }

        Cookie refreshToken = new Cookie("refreshToken", token.getRefreshToken());
        response.setHeader("Set-Cookie",
            "refreshToken=" + token.getRefreshToken() + "; Path=/; HttpOnly; Secure; Max-Age=" + refreshTokenExpired);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/token/reissue")
    public Object reissueAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        log.info("reissueAccessToken start");
        Cookie refreshToken = WebUtils.getCookie(request, "refreshToken");
        log.info("refreshToken: {}", refreshToken);
        Object responseJson = loginMemberService.reissue(refreshToken.getValue());
        log.info("tokenInfo: {}", responseJson);
        if (responseJson instanceof TokenInfo) {
            TokenInfo tokenInfo = (TokenInfo) responseJson;
            response.setHeader("Set-Cookie",
                "refreshToken=" + tokenInfo.getRefreshToken() + "; Path=/; HttpOnly; Secure; Max-Age="
                    + refreshTokenExpired);
        }
        return responseJson;
    }


}
