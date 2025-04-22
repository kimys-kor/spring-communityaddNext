package com.community.api.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record DmDto(
        Long id,
        @NotBlank(message = "제목은 필수입니다.")
        String title,
        @NotBlank(message = "내용은 필수입니다.")
        String content,
        @NotBlank(message = "받는 사람은 필수입니다.")
        String receiver
) {



}
