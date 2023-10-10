package stock.chart.login.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stock.chart.domain.Member;
import stock.chart.domain.RefreshToken;
import stock.chart.domain.RefreshTokenStatus;
import stock.chart.login.dto.LoginMemberRequestDto;
import stock.chart.login.dto.UsedTokenError;
import stock.chart.login.repository.LoginMemberRepository;
import stock.chart.login.repository.RefreshTokenRepository;
import stock.chart.security.JwtTokenProvider;
import stock.chart.security.dto.TokenInfo;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class LoginMemberService {

    private final LoginMemberRepository loginMemberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenInfo login(LoginMemberRequestDto loginMemberRequestDto) {
        log.info("login start");
        String memberEmail = loginMemberRequestDto.getMemberEmail();
        String password = loginMemberRequestDto.getPassword();

        // 1. MemberEmail, Password로 Member 조회
        Member member = loginMemberRepository.findByEmail(memberEmail)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
        if (!member.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }


        // 2. 토큰 생성
        TokenInfo tokenInfo = createNewToken(memberEmail, password);

        // 3. 저장소에 Refresh Token 저장
        saveRefreshToken(tokenInfo, member);
        log.info("end generateToken");
        return tokenInfo;
    }

    @Transactional
    public Object reissue(String refreshToken) {
        log.info("reissue start");
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        // Refresh Token Rotation 기법 사용, 유효한 Refresh Token인지 확인
        // 유효하지 않을 경우 Exception 발생
        // 유효할 경우 Refresh Token 가져와서 사용
        RefreshToken refreshTokenEntity = refreshTokenRotation(refreshToken);
        if (refreshTokenEntity.getStatus() == RefreshTokenStatus.INVALID) {
            return new UsedTokenError("토큰 탈취 에러", HttpStatus.BAD_REQUEST.toString(),"Refresh Token이 이미 사용되었습니다.");
        }
        refreshTokenEntity.updateStatus(RefreshTokenStatus.INVALID);

        // 3. 토큰 생성
        TokenInfo tokenInfo = createNewToken(refreshTokenEntity.getMember().getEmail(), refreshTokenEntity.getMember().getPassword());

        // 4. 저장소에 Refresh Token 저장
        saveRefreshToken(tokenInfo, refreshTokenEntity.getMember());

        log.info("tokenInfo: {}", tokenInfo);
        return tokenInfo;
    }

    /**
     * Refresh Token Rotation 기법 적용
     */
    private RefreshToken refreshTokenRotation(String refreshToken) {
        Optional<RefreshToken> token = refreshTokenRepository.findById(refreshToken);
        if (token.isEmpty()) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }
        RefreshToken refreshTokenEntity = token.get();
        if (refreshTokenEntity.getStatus() == RefreshTokenStatus.INVALID) {
            // 탈취된 토큰이 활용된 것이므로, 관련 멤버의 모든 Refresh Token을 무효화한다.
            // refresh 토큰은 한번만 사용할 수 있도록 한다.
            refreshTokenRepository.updateAllByMember(refreshTokenEntity.getMember(), RefreshTokenStatus.INVALID);
        }
        return refreshTokenEntity;
    }

    /**
     * 새로운 토큰 생성
     */
    private TokenInfo createNewToken(String memberEmail, String password) {
        log.info("createNewToken start");
        // 1. Refresh Token으로 MemberEmail 조회
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberEmail, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("authentication: {}", authentication);
        // 2. 새로운 토큰 생성 - refresh token도 새로 생성해서 나가게 된다.
        return jwtTokenProvider.generateToken(authentication);
    }

    /**
     * Refresh Token 저장
     */
    private void saveRefreshToken(TokenInfo tokenInfo, Member refreshTokenEntity) {
        log.info("saveRefreshToken start");
        RefreshToken newRefreshToken = new RefreshToken(tokenInfo.getRefreshToken(), RefreshTokenStatus.VALID,
            refreshTokenEntity);
        refreshTokenRepository.save(newRefreshToken);
    }


}
