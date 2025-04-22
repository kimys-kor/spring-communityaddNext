package com.community.api.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@ConfigurationProperties(prefix = "app.jwt")
@ConfigurationPropertiesBinding
public record JwtProperties(
        String secretKey,
        Long expirationTime,
        String headerString

) {
    public JwtProperties { // compact constructor
        // this -> null yet.
        if (expirationTime == null) {
            expirationTime = 7_200_000L;
        }
    }
}
