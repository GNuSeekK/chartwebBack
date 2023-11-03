package stock.chart.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import stock.chart.security.JwtTokenProvider;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secret;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        log.info("doFilterInternal start");
        String accesstoken = request.getHeader(HttpHeaders.AUTHORIZATION);
        Cookie refreshToken = WebUtils.getCookie(request, "refreshToken");
        if (accesstoken != null && accesstoken.startsWith(BEARER)) {
            String token = accesstoken.substring(BEARER.length());
            // token을 검증한다.
            try {
                if (jwtTokenProvider.validateToken(token)) {
                    // token이 유효하다면 token으로부터 유저 정보를 받아온다.
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    request.setAttribute("memberId", jwtTokenProvider.parseClaims(token).getSubject());
                    log.info("memberId: {}", jwtTokenProvider.parseClaims(token).getSubject());
                }
            } catch (ExpiredJwtException e) {
                log.info("ExpiredJwtException");
                // access token이 만료되었다면
                // 에러를 보낸다
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                Map<String, String> error = new HashMap<>();
                error.put("message", "access token expired");
                error.put("code", "401");
                error.put("status", "UNAUTHORIZED");
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(error));
                return;
            }

        } else if (request.getServletPath().equals("/login/token/reissue")) {
            // 재발급 요청일 경우 refreshToken 검증
            String token = refreshToken.getValue();
            try {
                if (jwtTokenProvider.validateToken(token)) {
                    // refresh token이 유효하다면
                    // refresh token으로부터 유저 정보를 받아온다.
                    request.setAttribute("memberId", jwtTokenProvider.parseClaims(token).getSubject());
                    request.setAttribute("refreshToken", token);
                }
            } catch (ExpiredJwtException e) {
                log.info("ExpiredJwtException");
                // refresh token이 만료되었다면 에러 보낸다
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                Map<String, String> error = new HashMap<>();
                error.put("message", "refresh token expired");
                error.put("code", "401");
                error.put("status", "UNAUTHORIZED");
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(error));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
