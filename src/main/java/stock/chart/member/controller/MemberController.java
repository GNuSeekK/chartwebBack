package stock.chart.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stock.chart.member.service.MemberService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/info")
    public Object getMemberInfo(@RequestHeader("Authorization") String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new RuntimeException("토큰이 존재하지 않습니다.");
        }
        if (!accessToken.startsWith("Bearer ")) {
            throw new RuntimeException("토큰이 올바르지 않습니다.");
        }
        accessToken = accessToken.substring(7);
        return memberService.getMemberInfo(accessToken);
    }
}
