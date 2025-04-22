package com.community.api.common.exception;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        String code,
        Integer status,
        String name,
        String message,
        LocalDateTime timestamp
) {
}