package stock.chart.login.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stock.chart.login.dto.MemberLoginRequestDto;
import stock.chart.login.service.MemberService;
import stock.chart.security.dto.TokenInfo;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public TokenInfo login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        log.info("memberLoginRequestDto: {}", memberLoginRequestDto);
        TokenInfo token = memberService.login(memberLoginRequestDto);
        log.info("token: {}", token);
        return token;
    }

    @PostMapping("/test")
    public String test() {
        return "test";
    }

}
