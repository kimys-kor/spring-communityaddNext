package com.community.api.service;

import com.community.api.common.exception.AuthenticationErrorCode;
import com.community.api.common.exception.CommonException;
import com.community.api.common.jwt.JwtTokenProvider;
import com.community.api.model.RefreshTokenEntity;
import com.community.api.repository.RefreshTokenRepository;
import com.community.api.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public void save(String username, String refreshToken) {

        RefreshTokenEntity tokenEntity = RefreshTokenEntity.builder()
                .refreshToken(refreshToken)
                .username(username)
                .build();

        refreshTokenRepository.deleteByUsernameEquals(username);
        refreshTokenRepository.save(tokenEntity);
    }

    public String refresh(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) throw new CommonException(AuthenticationErrorCode.UNKNOWN_REFRESH_TOKEN);
        String oldRefreshToken = Arrays.stream(cookies)
            .filter(eachCookie -> "refresh_token".equals(eachCookie.getName()))
            .findAny()
            .orElseThrow()
            .getValue();
        RefreshTokenEntity tokenEntity = refreshTokenRepository.findById(oldRefreshToken).orElseThrow(
                AuthenticationErrorCode.UNKNOWN_REFRESH_TOKEN::defaultException);
        String username = tokenEntity.getUsername();
        Long userId = userRepository.findByUsername(username).orElseThrow(
                AuthenticationErrorCode.AUTHENTICATION_FAILED::defaultException).getId();
        String accessToken = jwtTokenProvider.generateToken(userId, username);

        return accessToken;
    }

    public boolean existsByRefreshTokenAndEmail(String refreshToken, String email) {
        return refreshTokenRepository.existsByRefreshTokenAndUsername(refreshToken, email);
    }
}
