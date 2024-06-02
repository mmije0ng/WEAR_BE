package com.backend.wear.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

// JWT가 유효한 토큰인지 판단 (서명 확인, 만료된 토큰인지 등을 판단)
// 유효하다면 UserDetailService의 loadByUserName으로 해당 유저가 데이터베이스에 존재하는지 판단
// useDetails를 정상적으로 받아온다면 성공
// UserPasswordAuthenticationToken(스프링 시큐리티 내부에서 인가에 사용)을 생성하여
// 현재 요청의 Context에 추가
// 해당 요청이 필터를 거쳐 인가에 성공하여 승인된 request라는 뜻

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter { // OncePerRequestFilter -> 한 번 실행 보장

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    // JWT 토큰 검증 필터 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException{
        String authorizationHeader = request.getHeader("Authorization");

        //JWT가 헤더에 있는 경우
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            //JWT 유효성 검증
            try{
                jwtUtil.validateToken(token);
            } catch (ExpiredJwtException e){
                log.info("Expired JWT Token", e);

                // JWT 토큰 만료에 관한 응답을 클라이언트에게 보냄
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json; charset=UTF-8");

                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("access", false);
                responseBody.put("message", "AccessToken 만료");
                responseBody.put("timestamp", Instant.now().toString());

                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(responseBody);

                response.getWriter().write(jsonResponse);
                response.getWriter().flush();

                return;
            }

            Long userId = jwtUtil.getTokenUserId(token);
            log.info("jwt userId: "+userId);

            //유저와 토큰 일치 시 userDetails 생성
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId.toString());

            if (userDetails != null) {
                //UserDetsils, Password, Role -> 접근권한 인증 Token 생성
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                //현재 Request의 Security Context에 접근권한 설정
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response); // 다음 필터로 넘기기
    }
}