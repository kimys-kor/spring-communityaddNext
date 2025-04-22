package com.community.api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Builder
public record JoinRequestDto(
        @NotBlank(message = "username must not be blank")
        @Pattern(
                regexp = "[a-zA-Z0-9_]{3,16}",
                message = "유저네임은 3~16글자, 알파벳, 숫자, _만 사용 가능합니다."
        )
        String username,
        String password,
        String fullName,
        String phoneNum,
        String nickname
) {
    public JoinRequestDto {
        username = username.toLowerCase();
//        fullName = fullName.trim(); 앞뒤 공백제거
        fullName = fullName.strip(); // 유니코드상 공백이면 전부 제거 trim의 v2
    }

}