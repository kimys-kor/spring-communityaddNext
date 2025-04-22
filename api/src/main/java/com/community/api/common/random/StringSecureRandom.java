package com.community.api.common.random;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public final class StringSecureRandom {
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    private final SecureRandom secureRandom;

    public String next(int byteLength) {
        byte[] bytes = new byte[byteLength];
        secureRandom.nextBytes(bytes);

//        return new String(bytes); // 이렇게 안 함. 사용할 수 없는 표현들도 포함.
        return encoder.encodeToString(bytes); // 범용성(base64)
    }
}