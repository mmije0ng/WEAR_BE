package com.backend.wear.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
public class SecurityConfig  {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil,
                          CustomAccessDeniedHandler accessDeniedHandler, CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    // 인가가 필요하지 않는 경로
    private static final String[] AUTH_WHITELIST = {
            "/api/upload/**", "/api/products/category/**", "/api/products/search/category/**", "/api/products/search/rank/**",
            "/test","/ws-stomp/**",
            "/api/university/**","/api/auth/**","/api/token/**",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui/**"
        };

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{
        // CSRF
        http.csrf((csrf) -> csrf.disable());

        // CORS
        http.cors(httpSecurityCorsConfigurer ->
                httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource())
        );

        //세션 관리 상태 없음으로 구성, Spring Security가 세션 생성 or 사용 X
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        //FormLogin, BasicHttp 비활성화
     //   http.formLogin((form) -> form.disable());
     //   http.httpBasic(AbstractHttpConfigurer::disable);

        //JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        http.addFilterBefore(new JwtAuthFilter(customUserDetailsService, jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling((exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        );

        // 권한 규칙 작성
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AUTH_WHITELIST).permitAll()
             //           .anyRequest().permitAll()
                        .anyRequest().authenticated()
        );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsconfiguration = new CorsConfiguration();

        corsconfiguration.addAllowedOrigin("http://localhost:8080");
        corsconfiguration.addAllowedOrigin("http://localhost:5173");
        corsconfiguration.addAllowedOrigin("http://43.201.189.171:8080");
        corsconfiguration.addAllowedOrigin("http://wear-frontend.s3-website.ap-northeast-2.amazonaws.com");
 //       configuration.addAllowedOrigin("https://jiangxy.github.io");

//        configuration.setAllowedOriginPatterns(List.of("http://localhost:8080",  "http://localhost:5173",
//                "http://43.201.189.171:8080", "http://wear-frontend.s3-website.ap-northeast-2.amazonaws.com"
//        ));

        //허용할 헤더 설정
        corsconfiguration.addAllowedHeader("*");

        //허용할 http method
        corsconfiguration.addAllowedMethod("*");

        // 클라이언트가 접근 할 수 있는 서버 응답 헤더
        corsconfiguration.addExposedHeader("Authorization");

        //사용자 자격 증명이 지원되는지 여부
        corsconfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsconfiguration);

        return source;

//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//
//        corsConfiguration.setAllowedOriginPatterns(List.of("http://localhost:8080",  "http://localhost:5173",
//                "http://43.201.189.171:8080", "http://wear-frontend.s3-website.ap-northeast-2.amazonaws.com"
//        ));
//
//        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
//
//        corsConfiguration.setAllowedMethods(List.of("*"));
//        corsConfiguration.setAllowedHeaders(List.of("*"));
//        corsConfiguration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration); // 모든 경로에 대해서 CORS 설정을 적용
//
//        return source;
    }
}