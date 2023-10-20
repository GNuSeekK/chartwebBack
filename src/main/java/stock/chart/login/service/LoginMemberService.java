package stock.chart.login.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import stock.chart.domain.Member;
import stock.chart.domain.RefreshToken;
import stock.chart.domain.RefreshTokenStatus;
import stock.chart.login.dto.LoginMemberForm;
import stock.chart.login.exception.KaKaoLoginFailException;
import stock.chart.login.exception.MemberNotMatchException;
import stock.chart.login.exception.NeedRegisterMemberException;
import stock.chart.login.exception.RefreshTokenInvalidException;
import stock.chart.login.exception.UsedTokenException;
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
    private final ObjectMapper objectMapper;

    @Value("${oauth.kakao.client-id}")
    private String kakaoClientId;
    @Value("${oauth.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Transactional
    public TokenInfo login(LoginMemberForm loginMemberForm) {
        log.info("login start");
        String memberEmail = loginMemberForm.getEmail();
        String password = loginMemberForm.getPassword();

        // 1. MemberEmail, Password로 Member 조회
        Member member = loginMemberRepository.findByEmail(memberEmail)
            .orElseThrow(MemberNotMatchException::new);
        if (!member.getPassword().equals(password)) {
            throw new MemberNotMatchException();
        }
        // 2. 토큰 생성
        TokenInfo tokenInfo = createNewToken(member.getId(), password);
        // 3. 저장소에 Refresh Token 저장
        saveRefreshToken(tokenInfo, member);
        log.info("end generateToken");
        return tokenInfo;
    }

    @Transactional(noRollbackFor = UsedTokenException.class) // UsedTokenException 발생시 rollback 하지 않음
    public TokenInfo reissue(String refreshToken) {
        log.info("reissue start");
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RefreshTokenInvalidException();
        }

        // Refresh Token Rotation 기법 사용, 유효한 Refresh Token인지 확인
        // 유효하지 않을 경우 Exception 발생
        // 유효할 경우 Refresh Token 가져와서 사용
        RefreshToken refreshTokenEntity = refreshTokenRotation(refreshToken);
        if (refreshTokenEntity.getStatus() == RefreshTokenStatus.INVALID) {
            throw new UsedTokenException();
        }
        refreshTokenEntity.updateStatus(RefreshTokenStatus.INVALID);
        // 3. 토큰 생성
        TokenInfo token = createNewToken(jwtTokenProvider.getMemberId(refreshToken),
            refreshTokenEntity.getMember().getPassword());
        // 4. 저장소에 Refresh Token 저장
        saveRefreshToken(token, refreshTokenEntity.getMember());
        return token;
    }

    /**
     * Refresh Token Rotation 기법 적용
     */
    private RefreshToken refreshTokenRotation(String refreshToken) {
        log.info("refreshTokenRotation start");
        Optional<RefreshToken> token = refreshTokenRepository.findFetchByRefreshToken(refreshToken);
        if (token.isEmpty()) {
            throw new RefreshTokenInvalidException();
        }
        log.info("token: {}", token);
        RefreshToken refreshTokenEntity = token.get();
        log.info("refreshTokenEntity: {}", refreshTokenEntity);
        if (refreshTokenEntity.getStatus() == RefreshTokenStatus.INVALID) {
            // 탈취된 토큰이 활용된 것이므로, 관련 멤버의 모든 Refresh Token을 무효화한다.
            // refresh 토큰은 한번만 사용할 수 있도록 한다.
            refreshTokenRepository.updateAllByMember(refreshTokenEntity.getMember(), RefreshTokenStatus.INVALID);
        }
        log.info("end refreshTokenRotation");
        return refreshTokenEntity;
    }

    /**
     * 새로운 토큰 생성
     */
    private TokenInfo createNewToken(String id, String password) {
        log.info("createNewToken start");
        // 1. Refresh Token으로 MemberEmail 조회
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id,
            password);
        log.info("authenticationToken: {}", authenticationToken);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("authentication: {}", authentication);
        // 2. 새로운 토큰 생성 - refresh token도 새로 생성해서 나가게 된다.
        return jwtTokenProvider.generateToken(authentication);
    }

    /**
     * createNewToken 오버로딩
     */
    private TokenInfo createNewToken(Long id, String password) {
        return createNewToken(id.toString(), password);
    }

    /**
     * Refresh Token 저장
     */
    private void saveRefreshToken(TokenInfo tokenInfo, Member member) {
        log.info("saveRefreshToken start");
        RefreshToken newRefreshToken = new RefreshToken(tokenInfo.getRefreshToken(), RefreshTokenStatus.VALID, member);
        refreshTokenRepository.save(newRefreshToken);
    }


    @Transactional
    public TokenInfo kakaoLogin(String code) {
        // kakao로 부터 받은 코드로 email 가져오기
        String email = getEmailFromCode(code);
        Member member;
        // kakao email로 회원 조회
        Optional<Member> kakaoMember = loginMemberRepository.findByKakaoEmail(email);
        if (kakaoMember.isEmpty()) { // 없으면 email로 조회
            Optional<Member> memberOptional = loginMemberRepository.findByEmail(email);
            if (memberOptional.isPresent()) { // 있으면 kakao email에 업데이트
                member = memberOptional.get();
                member.changeKakaoEmail(email);
            } else { // 없으면 회원가입
                throw new NeedRegisterMemberException(email);
            }
        } else { // 있으면 그냥 로그인
            member = kakaoMember.get();
        }
        // 자체 토큰 생성
        TokenInfo tokenInfo = createNewToken(member.getId(), member.getPassword());
        saveRefreshToken(tokenInfo, member);
        return tokenInfo;
    }

    private String getEmailFromCode(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);
        RestTemplate restTemplate = new RestTemplate();
        // 엑세스 토큰 발급 요청
        ResponseEntity<String> response = restTemplate.exchange(
            "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            new HttpEntity<>(params, headers),
            String.class
        );

        if (response.getStatusCode() != HttpStatus.OK) { // 엑세스 토큰 발급 실패
            throw new KaKaoLoginFailException();
        }

        String accessToken;
        try { // 엑세스 토큰 가져오기
            accessToken = objectMapper.readTree(response.getBody()).get("access_token").asText();
        } catch (JsonProcessingException e) {
            throw new KaKaoLoginFailException();
        }

        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        response = restTemplate.exchange( // 카카오 로그인 정보 가져오기
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class
        );
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new KaKaoLoginFailException();
        }
        String email;
        try { // 카카오 로그인 정보에서 이메일 가져오기
            email = objectMapper.readTree(response.getBody()).get("kakao_account").get("email").asText();
        } catch (JsonProcessingException e) {
            throw new KaKaoLoginFailException();
        }
        return email;
    }


}
