package com.community.api.model.dto;

import jakarta.validation.constraints.NotBlank;

public record SaveIpDto(
        @NotBlank
        String type,
        Long id,
        @NotBlank
        String ipAddress
) {
}
