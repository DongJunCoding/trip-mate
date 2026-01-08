package com.server.backend.common.auth.security.config;

import com.server.backend.common.auth.api.service.JWTService;
import com.server.backend.common.auth.hadler.RefreshTokenLogoutHandler;
import com.server.backend.common.auth.jwt.filter.JWTFilter;
import com.server.backend.common.auth.jwt.filter.LoginFilter;
import com.server.backend.common.auth.jwt.util.JWTUtil;
import com.server.backend.common.data.enums.UserRoleType;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final AuthenticationSuccessHandler loginSuccessHandler;
    private final AuthenticationSuccessHandler socialSuccessHandler;
    private final JWTService jwtService;
    private final JWTUtil jwtUtil;

    public SecurityConfig(
            AuthenticationConfiguration authenticationConfiguration,
            @Qualifier("LoginSuccessHandler") AuthenticationSuccessHandler loginSuccessHandler,
            @Qualifier("SocialSuccessHandler") AuthenticationSuccessHandler socialSuccessHandler,
            JWTService jwtService,
            JWTUtil jwtUtil
    ) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.loginSuccessHandler = loginSuccessHandler;
        this.socialSuccessHandler = socialSuccessHandler;
        this.jwtService = jwtService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withRolePrefix("ROLE_")
                .role(UserRoleType.SYS.name()).implies(UserRoleType.USER.name())
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS 설정
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // CSRF 보안 필터 disable
        http
                .csrf(AbstractHttpConfigurer::disable);

        // 기본 Form 기반 인증 필터 disable
        http
                .formLogin(AbstractHttpConfigurer::disable);

        // 기본 Basic 인증 필터 disable
        http
                .httpBasic(AbstractHttpConfigurer::disable);

        // 기본 로그아웃 필터 + 커스텀 Refresh 토큰 삭제 핸들러 추가
        http
                .logout(logout -> logout
                        .addLogoutHandler(new RefreshTokenLogoutHandler(jwtService, jwtUtil)));

        // OAuth2 인증용
        http
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(socialSuccessHandler));

        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 인가
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/app/**").hasAnyRole("SYS", "USER")
                        .requestMatchers("/api/v1/common/auth/reissue").permitAll()
                        .anyRequest().permitAll());

        // 인가
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/jwt/exchange", "/jwt/refresh").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/user/exist", "/user").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/user").hasRole(UserRoleType.USER.name())
//                        .requestMatchers(HttpMethod.PUT, "/user").hasRole(UserRoleType.USER.name())
//                        .requestMatchers(HttpMethod.DELETE, "/user").hasRole(UserRoleType.USER.name())
//                        .anyRequest().authenticated()
//                );

        // 예외처리
        http
                .exceptionHandling(e -> e
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // 로그인 하지 않은 상태로 접근시
                })
                                .accessDeniedHandler((request, response, authException) -> {
                                    response.sendError(HttpServletResponse.SC_FORBIDDEN); // 로그인 하였지만 권한이 없을시
                                })
                );

        // 커스텀 필터 추가
        http
                .addFilterBefore(new JWTFilter(jwtUtil), LogoutFilter.class);

        http
                .addFilterBefore(new LoginFilter(authenticationManager(authenticationConfiguration), loginSuccessHandler), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
