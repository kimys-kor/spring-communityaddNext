package com.community.api.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
public record LoginRequestDto(
        @NotBlank(message = "username must not be blank")
        String username,
        String password
) {
    public LoginRequestDto {
        username = username.toLowerCase();
    }
}

