package com.community.api.common.security;

import com.community.api.common.jwt.JwtAuthorizationFilter;
import com.community.api.common.jwt.JwtTokenProvider;
import com.community.api.common.properties.JwtProperties;
import com.community.api.common.jwt.JwtAuthenticationFilter;
import com.community.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@RequiredArgsConstructor
public class SecurityConfig  {

    private final UserRepository userRepository;
    private final CorsConfig corsConfig;
    private final JwtProperties jwtProperties;
    private final JwtTokenProvider jwtTokenProvider;


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new MyCustomDsl()) // 커스텀 필터 등록
                .and()
                .authorizeRequests(authroize -> authroize
                        .requestMatchers("/user/login","/user/join", "/user/refresh", "/board/**")
                        .access("permitAll()")
                        .requestMatchers("/user/**")
                        .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_MASTER')")
                        .requestMatchers("/admin/**")
                        .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MASTER')")
                        .requestMatchers("/master/**")
                        .access("hasRole('ROLE_MASTER')")
                        .anyRequest().permitAll())
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration
                                                        authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }


    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager,  jwtProperties, jwtTokenProvider))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository, jwtProperties, jwtTokenProvider));
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecureRandom secureRandom() throws NoSuchAlgorithmException {
        return SecureRandom.getInstanceStrong();
    }
}