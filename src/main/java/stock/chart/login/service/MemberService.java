package stock.chart.login.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stock.chart.login.dto.MemberLoginRequestDto;
import stock.chart.login.repository.MemberRepository;
import stock.chart.security.JwtTokenProvider;
import stock.chart.security.dto.TokenInfo;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenInfo login(MemberLoginRequestDto memberLoginRequestDto) {
        log.info("login start");
        String memberEmail = memberLoginRequestDto.getMemberEmail();
        String password = memberLoginRequestDto.getPassword();

        // 1. ID/PW를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberEmail, password);
        log.info("authenticationToken: {}", authenticationToken);
        // 2. AuthenticationToken을 기반으로 Authentication 객체 생성
        log.info("start authentication");
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        log.info("authentication: {}", authentication);
        // 3. Authentication 객체를 기반으로 JWT Token 생성 후, Response
        log.info("start generateToken");
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        log.info("end generateToken");
        return tokenInfo;
    }

}
