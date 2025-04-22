package com.community.api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SavePostDto(
        @NotNull(message = "postType must not be blank")
        int postType,
        @NotNull(message = "notification must not be blank")
        boolean notification,
        @NotBlank(message = "title must not be blank")
        String title,
        @NotBlank(message = "content must not be blank")
        String content,
        String thumbNail
) {

}
