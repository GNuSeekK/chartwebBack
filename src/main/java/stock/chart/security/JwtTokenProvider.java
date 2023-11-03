package stock.chart.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import stock.chart.security.dto.TokenInfo;

@Slf4j
@Component
public class JwtTokenProvider {


    private Key secret;
    @Value("${jwt.access-expired}")
    private Long accessTokenExpired;
    @Value("${jwt.refresh-expired}")
    private Long refreshTokenExpired;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = secret.getBytes();
        this.secret = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 유저 정보를 통해 토큰 생성
     */
    public TokenInfo generateToken(Authentication authentication) {
        log.info("generateToken start");

        long now = new Date(System.currentTimeMillis()).getTime();
        Date accessTokenExpiresIn = new Date(now + (1000 * accessTokenExpired)); // 30분
        Date refreshTokenExpiresIn = new Date(now + (1000 * refreshTokenExpired)); // 14일

        String accessToken = Jwts.builder()
            .setSubject(authentication.getName())
            .claim("auth", "USER")
            .setExpiration(accessTokenExpiresIn)
            .signWith(secret, SignatureAlgorithm.HS256)
            .compact();

        log.info(parseClaims(accessToken).toString());

        String refreshToken = Jwts.builder()
            .setSubject(authentication.getName())
            .setExpiration(refreshTokenExpiresIn)
            .signWith(secret, SignatureAlgorithm.HS256)
            .compact();

        log.info("accessToken: {}", accessToken);
        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 토큰에서 유저 정보 추출
     */
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        log.info("claims: {}", claims);
        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        log.info("authorities: {}", authorities);
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * 토큰 정보 검증
     */
    public boolean validateToken(String token) {
        log.info("validateToken start");
        log.info("token: {}", token);
        try {
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            // refresh token 활용해서 재발급
            log.info("Expired JWT Token", e);
            throw e;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
               .setSigningKey(secret)
               .build()
               .parseClaimsJws(token)
               .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
