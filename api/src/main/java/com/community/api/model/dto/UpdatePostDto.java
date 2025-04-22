package com.community.api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdatePostDto(
        @NotNull(message = "postId must not be blank")
        Long postId,
        boolean notification,
        @NotBlank(message = "title must not be blank")
        String title,
        @NotBlank(message = "content must not be blank")
        String content,
        String thumbNail
) {
        public static class UpdatePostDtoBuilder {
                private boolean notification = false;
        }
}