package com.community.api.common.jwt;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.community.api.common.exception.AuthenticationErrorCode;
import com.community.api.common.properties.JwtProperties;
import com.community.api.common.security.PrincipalDetails;
import com.community.api.model.User;
import com.community.api.model.base.UserStatus;
import com.community.api.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

// 인가
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;
    private JwtProperties jwtProperties;
    private JwtTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository,
                                  JwtProperties jwtProperties, JwtTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtProperties = jwtProperties;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String Authorization = request.getHeader(jwtProperties.headerString());
        if (Authorization == null || !Authorization.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = Authorization.replace("Bearer ", "");

        try {
            String username = jwtTokenProvider.resolveToken(token);

            if (username != null) {
                User user = userRepository.findByUsername(username).orElseThrow(()-> new AuthenticationException("없는 회원 입니다") {
                });

                if (!user.getStatus().equals(UserStatus.NORMAL)) {
                    throw new ServletException("없는 회원입니다");
                }

                // 인증은 토큰 검증시 끝. 인증을 하기 위해서가 아닌 스프링 시큐리티가 수행해주는 권한 처리를 위해
                // 아래와 같이 토큰을 만들어서 Authentication 객체를 강제로 만들고 그걸 세션에 저장!
                PrincipalDetails principalDetails = new PrincipalDetails(user);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        principalDetails, // 나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                        null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
                        principalDetails.getAuthorities());

                // 강제로 시큐리티의 세션에 접근하여 값 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new ServletException("Invalid token: Username claim is missing.");
            }


        } catch (TokenExpiredException e) {
            request.setAttribute("exception", AuthenticationErrorCode.EXPIRED_TOKEN.name());
        } catch (JWTVerificationException e) {
            request.setAttribute("exception", AuthenticationErrorCode.INVALID_TOKEN.name());
        }
        chain.doFilter(request, response);
    }

}
